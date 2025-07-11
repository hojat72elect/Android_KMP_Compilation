package ca.hojat.smart.musicplayer.home.settings

import android.annotation.SuppressLint
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.View
import android.view.Gravity
import android.view.ViewGroup
import android.widget.PopupMenu
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.shared.ui.views.MyRecyclerViewAdapter
import ca.hojat.smart.musicplayer.shared.BaseSimpleActivity
import ca.hojat.smart.musicplayer.shared.extensions.getPopupMenuTheme
import ca.hojat.smart.musicplayer.shared.extensions.getProperTextColor
import ca.hojat.smart.musicplayer.shared.extensions.humanizePath
import ca.hojat.smart.musicplayer.shared.extensions.setupViewBackground
import ca.hojat.smart.musicplayer.shared.data.RefreshRecyclerViewListener
import ca.hojat.smart.musicplayer.shared.ui.views.MyRecyclerView
import ca.hojat.smart.musicplayer.databinding.ItemExcludedFolderBinding
import ca.hojat.smart.musicplayer.shared.extensions.config

class ExcludedFoldersAdapter(
    activity: BaseSimpleActivity,
    var folders: ArrayList<String>,
    val listener: RefreshRecyclerViewListener?,
    recyclerView: MyRecyclerView,
    itemClick: (Any) -> Unit
) : MyRecyclerViewAdapter(activity, recyclerView, itemClick) {

    private val config = activity.config

    init {
        setupDragListener()
    }

    override fun getActionMenuId() = R.menu.cab_remove_only

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {
        when (id) {
            R.id.cab_remove -> removeSelection()
        }
    }

    override fun getSelectableItemCount() = folders.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = folders.getOrNull(position)?.hashCode()

    override fun getItemKeyPosition(key: Int) = folders.indexOfFirst { it.hashCode() == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExcludedFolderBinding.inflate(layoutInflater, parent, false)
        return createViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        holder.bindView(folder, allowSingleClick = true, allowLongClick = true) { itemView, _ ->
            setupView(itemView, folder)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = folders.size

    private fun getSelectedItems() = folders.filter { selectedKeys.contains(it.hashCode()) } as ArrayList<String>

    private fun setupView(view: View, folder: String) {
        ItemExcludedFolderBinding.bind(view).apply {
            root.setupViewBackground(activity)
            excludedFolderHolder.isSelected = selectedKeys.contains(folder.hashCode())
            excludedFolderTitle.apply {
                @SuppressLint("SetTextI18n")
                text = context.humanizePath(folder) + "/"
                setTextColor(context.getProperTextColor())
            }

            overflowMenuIcon.drawable.apply {
                mutate()
                setTint(activity.getProperTextColor())
            }

            overflowMenuIcon.setOnClickListener {
                showPopupMenu(overflowMenuAnchor, folder)
            }
        }
    }

    private fun showPopupMenu(view: View, folder: String) {
        finishActMode()
        val theme = activity.getPopupMenuTheme()
        val contextTheme = ContextThemeWrapper(activity, theme)

        PopupMenu(contextTheme, view, Gravity.END).apply {
            inflate(getActionMenuId())
            setOnMenuItemClickListener { item ->
                val eventTypeId = folder.hashCode()
                when (item.itemId) {
                    R.id.cab_remove -> {
                        executeItemMenuOperation(eventTypeId) {
                            removeSelection()
                        }
                    }
                }
                true
            }
            show()
        }
    }

    private fun executeItemMenuOperation(eventTypeId: Int, callback: () -> Unit) {
        selectedKeys.clear()
        selectedKeys.add(eventTypeId)
        callback()
    }

    private fun removeSelection() {
        val removeFolders = ArrayList<String>(selectedKeys.size)
        val positions = getSelectedItemPositions()

        getSelectedItems().forEach {
            removeFolders.add(it)
            config.removeExcludedFolder(it)
        }

        folders.removeAll(removeFolders.toSet())
        removeSelectedItems(positions)
        if (folders.isEmpty()) {
            listener?.refreshItems()
        }
    }
}
