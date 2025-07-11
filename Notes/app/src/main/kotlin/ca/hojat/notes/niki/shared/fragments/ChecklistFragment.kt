package ca.hojat.notes.niki.shared.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ca.hojat.notes.niki.databinding.FragmentChecklistBinding
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.shared.data.models.ChecklistItem
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.dialogs.NewChecklistItemDialog
import ca.hojat.notes.niki.shared.extensions.beVisibleIf
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getProperPrimaryColor
import ca.hojat.notes.niki.shared.extensions.getProperTextColor
import ca.hojat.notes.niki.shared.extensions.hideKeyboard
import ca.hojat.notes.niki.shared.extensions.underlineText
import ca.hojat.notes.niki.shared.extensions.updateWidgets
import ca.hojat.notes.niki.shared.helpers.NOTE_ID
import ca.hojat.notes.niki.shared.helpers.NotesHelper
import ca.hojat.notes.niki.shared.helpers.SORT_BY_CUSTOM
import ca.hojat.notes.niki.shared.helpers.ensureBackgroundThread
import ca.hojat.notes.niki.shared.interfaces.ChecklistItemsListener
import ca.hojat.notes.niki.shared.ui.adapters.ChecklistAdapter
import java.io.File

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class ChecklistFragment : NoteFragment(), ChecklistItemsListener {

    private var noteId = 0L

    private lateinit var binding: FragmentChecklistBinding

    var items = mutableListOf<ChecklistItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChecklistBinding.inflate(inflater, container, false)
        noteId = requireArguments().getLong(NOTE_ID, 0L)
        setupFragmentColors()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadNoteById(noteId)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)

        if (menuVisible) {
            activity?.hideKeyboard()
        } else if (::binding.isInitialized) {
            (binding.checklistList.adapter as? ChecklistAdapter)?.finishActMode()
        }
    }

    private fun loadNoteById(noteId: Long) {
        NotesHelper(requireActivity()).getNoteWithId(noteId) { storedNote ->
            if (storedNote != null && activity?.isDestroyed == false) {
                note = storedNote

                try {
                    val checklistItemType = object : TypeToken<List<ChecklistItem>>() {}.type
                    items = Gson().fromJson<ArrayList<ChecklistItem>>(
                        storedNote.getNoteStoredValue(requireActivity()), checklistItemType
                    ) ?: ArrayList(1)

                    // checklist title can be null only because of the glitch in upgrade to 6.6.0, remove this check in the future
                    items = items.toMutableList() as ArrayList<ChecklistItem>
                    val sorting = config?.sorting ?: 0
                    if (sorting and SORT_BY_CUSTOM == 0 && config?.moveDoneChecklistItems == true) {
                        items.sortBy { it.isDone }
                    }

                    setupFragment()
                } catch (e: Exception) {
                    migrateCheckListOnFailure(storedNote)
                }
            }
        }
    }

    private fun migrateCheckListOnFailure(note: Note) {
        items.clear()

        note.getNoteStoredValue(requireActivity())?.split("\n")?.map { it.trim() }
            ?.filter { it.isNotBlank() }?.forEachIndexed { index, value ->
                items.add(
                    ChecklistItem(
                        id = index,
                        title = value,
                        isDone = false
                    )
                )
            }

        saveChecklist()
    }

    private fun setupFragment() {
        if (activity == null || requireActivity().isFinishing) {
            return
        }

        setupFragmentColors()
        checkLockState()
        setupAdapter()
    }

    private fun setupFragmentColors() {
        val adjustedPrimaryColor = requireActivity().getProperPrimaryColor()
        binding.checklistFab.apply {
            setColors(
                adjustedPrimaryColor
            )

            setOnClickListener {
                showNewItemDialog()
                (binding.checklistList.adapter as? ChecklistAdapter)?.finishActMode()
            }
        }

        binding.fragmentPlaceholder.setTextColor(requireActivity().getProperTextColor())
        binding.fragmentPlaceholder2.apply {
            setTextColor(adjustedPrimaryColor)
            underlineText()
            setOnClickListener {
                showNewItemDialog()
            }
        }
    }

    override fun checkLockState() {
        if (note == null) {
            return
        }

        binding.apply {
            checklistContentHolder.beVisibleIf(!note!!.isLocked() || shouldShowLockedContent)
            checklistFab.beVisibleIf(!note!!.isLocked() || shouldShowLockedContent)
            setupLockedViews(this.toCommonBinding(), note!!)
        }
    }

    private fun showNewItemDialog() {
        NewChecklistItemDialog(activity as BaseActivity) { titles ->
            var currentMaxId = items.maxByOrNull { item -> item.id }?.id ?: 0
            val newItems = ArrayList<ChecklistItem>()

            titles.forEach { title ->
                title.split("\n").map { it.trim() }.filter { it.isNotBlank() }.forEach { row ->
                    newItems.add(
                        ChecklistItem(
                            currentMaxId + 1,
                            System.currentTimeMillis(),
                            row,
                            false
                        )
                    )
                    currentMaxId++
                }
            }

            if (config?.addNewChecklistItemsTop == true) {
                items.addAll(0, newItems)
            } else {
                items.addAll(newItems)
            }

            saveNote()
            setupAdapter()
        }
    }

    private fun setupAdapter() {
        updateUIVisibility()
        ChecklistItem.sorting = requireContext().config.sorting
        if (ChecklistItem.sorting and SORT_BY_CUSTOM == 0) {
            items.sort()
            if (context?.config?.moveDoneChecklistItems == true) {
                items.sortBy { it.isDone }
            }
        }
        ChecklistAdapter(
            activity = activity as BaseActivity,
            items = items,
            listener = this,
            recyclerView = binding.checklistList,
            showIcons = true
        ) { item ->
            val clickedNote = item as ChecklistItem
            clickedNote.isDone = !clickedNote.isDone

            saveNote(items.indexOfFirst { it.id == clickedNote.id })
            context?.updateWidgets()
        }.apply {
            binding.checklistList.adapter = this
        }
    }

    private fun saveNote(refreshIndex: Int = -1, callback: () -> Unit = {}) {
        if (note == null) {
            return
        }

        if (note!!.path.isNotEmpty() && !note!!.path.startsWith("content://") && !File(note!!.path).exists()) {
            return
        }

        if (context == null || activity == null) {
            return
        }

        if (note != null) {
            if (refreshIndex != -1) {
                binding.checklistList.post {
                    binding.checklistList.adapter?.notifyItemChanged(refreshIndex)
                }
            }

            note!!.value = getChecklistItems()

            ensureBackgroundThread {
                saveNoteValue(note!!, note!!.value)
                context?.updateWidgets()
                activity?.runOnUiThread(callback)
            }
        }
    }

    fun removeDoneItems() {
        items = items.filter { !it.isDone }.toMutableList() as ArrayList<ChecklistItem>
        saveNote()
        setupAdapter()
    }

    private fun updateUIVisibility() {
        binding.apply {
            fragmentPlaceholder.beVisibleIf(items.isEmpty())
            fragmentPlaceholder2.beVisibleIf(items.isEmpty())
            checklistList.beVisibleIf(items.isNotEmpty())
        }
    }

    fun getChecklistItems(): String = Gson().toJson(items)

    override fun saveChecklist(callback: () -> Unit) {
        saveNote(callback = callback)
    }

    override fun refreshItems() {
        loadNoteById(noteId)
        setupAdapter()
    }

    private fun FragmentChecklistBinding.toCommonBinding(): CommonNoteBinding = this.let {
        object : CommonNoteBinding {
            override val root: View = it.root
            override val noteLockedLayout: View = it.noteLockedLayout
            override val noteLockedImage: ImageView = it.noteLockedImage
            override val noteLockedLabel: TextView = it.noteLockedLabel
            override val noteLockedShow: TextView = it.noteLockedShow
        }
    }
}
