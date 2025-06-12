package ca.hojat.notes.niki.shared.ui.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ca.hojat.notes.niki.R
import ca.hojat.notes.niki.shared.activities.BaseActivity
import ca.hojat.notes.niki.databinding.OpenNoteItemBinding
import ca.hojat.notes.niki.shared.extensions.beGoneIf
import ca.hojat.notes.niki.shared.extensions.beVisibleIf
import ca.hojat.notes.niki.shared.extensions.config
import ca.hojat.notes.niki.shared.extensions.getColoredDrawableWithColor
import ca.hojat.notes.niki.shared.extensions.isBlackAndWhiteTheme
import ca.hojat.notes.niki.shared.helpers.LOWER_ALPHA_INT
import ca.hojat.notes.niki.shared.helpers.SORT_BY_CUSTOM
import ca.hojat.notes.niki.shared.data.models.ChecklistItem
import ca.hojat.notes.niki.shared.data.models.Note
import ca.hojat.notes.niki.shared.data.models.NoteType
import ca.hojat.notes.niki.shared.views.MyRecyclerView

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class OpenNoteAdapter(
    activity: BaseActivity, var items: List<Note>,
    recyclerView: MyRecyclerView, itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, itemClick) {
    override fun getActionMenuId() = 0

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = itemCount

    override fun getIsItemSelectable(position: Int) = false

    override fun getItemSelectionKey(position: Int) = items.getOrNull(position)?.id?.toInt()

    override fun getItemKeyPosition(key: Int) = items.indexOfFirst { it.id?.toInt() == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun prepareActionMode(menu: Menu) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return createViewHolder(OpenNoteItemBinding.inflate(layoutInflater, parent, false).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(
            any = item,
            allowSingleClick = true,
            allowLongClick = false
        ) { itemView, _ ->
            setupView(itemView, item)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = items.size

    private fun setupView(view: View, note: Note) {
        OpenNoteItemBinding.bind(view).apply {
            root.setupCard()
            openNoteItemTitle.apply {
                text = note.title
                setTextColor(properPrimaryColor)
            }
            val formattedText = note.getFormattedValue(root.context)
            openNoteItemText.beGoneIf(formattedText.isNullOrBlank() || note.isLocked())
            iconLock.beVisibleIf(note.isLocked())
            iconLock.setImageDrawable(
                activity.resources.getColoredDrawableWithColor(
                    R.drawable.ic_lock_vector,
                    properPrimaryColor
                )
            )
            openNoteItemText.apply {
                text = formattedText
                setTextColor(textColor)
            }
        }
    }

    private fun View.setupCard() {
        if (context.isBlackAndWhiteTheme()) {
            setBackgroundResource(R.drawable.black_dialog_background)
        } else {
            val cardBackgroundColor = if (backgroundColor == Color.BLACK) {
                Color.WHITE
            } else {
                Color.BLACK
            }
            val cardBackground = if (context.config.isUsingSystemTheme) {
                R.drawable.dialog_you_background
            } else {
                R.drawable.dialog_bg
            }
            background =
                activity.resources.getColoredDrawableWithColor(
                    cardBackground,
                    cardBackgroundColor,
                    LOWER_ALPHA_INT
                )
        }
    }

    private fun Note.getFormattedValue(context: Context): CharSequence? {
        return when (type) {
            NoteType.TYPE_TEXT -> getNoteStoredValue(context)
            NoteType.TYPE_CHECKLIST -> {
                val checklistItemType = object : TypeToken<List<ChecklistItem>>() {}.type
                var items = Gson().fromJson<List<ChecklistItem>>(
                    getNoteStoredValue(context),
                    checklistItemType
                ) ?: listOf()
                items = items.let {
                    val sorting = context.config.sorting
                    ChecklistItem.sorting = sorting
                    if (ChecklistItem.sorting and SORT_BY_CUSTOM == 0) {
                        it.sorted().let {
                            if (context.config.moveDoneChecklistItems) {
                                it.sortedBy { it.isDone }
                            } else {
                                it
                            }
                        }
                    } else {
                        it
                    }
                }
                val linePrefix = "• "
                val stringifiedItems = items.joinToString(separator = System.lineSeparator()) {
                    "${linePrefix}${it.title}"
                }

                val formattedText = SpannableString(stringifiedItems)
                var currentPos = 0
                items.forEach { item ->
                    currentPos += linePrefix.length
                    if (item.isDone) {
                        formattedText.setSpan(
                            StrikethroughSpan(),
                            currentPos,
                            currentPos + item.title.length,
                            0
                        )
                    }
                    currentPos += item.title.length
                    currentPos += System.lineSeparator().length
                }
                formattedText
            }
        }
    }
}
