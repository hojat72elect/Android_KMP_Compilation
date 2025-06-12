package com.amaze.fileutilities.home_page.ui.analyse

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.ui.files.MediaAdapterPreloader
import com.amaze.fileutilities.home_page.ui.files.MediaFileInfo
import com.amaze.fileutilities.home_page.ui.files.MediaInfoRecyclerViewHolder
import com.amaze.fileutilities.utilis.AbstractMediaFilesAdapter
import com.amaze.fileutilities.utilis.EmptyViewHolder
import com.amaze.fileutilities.utilis.FileUtils
import com.amaze.fileutilities.utilis.Utils
import java.lang.ref.WeakReference

class ReviewAnalysisAdapter(
    val context: Context,
    private val analysisType: Int?,
    private val preloader: MediaAdapterPreloader<MediaFileInfo>,
    private val mediaFileInfoList: MutableList<MediaFileInfo>,
    toggleCheckCallback: (
        checkedSize: Int,
        itemsCount: Int,
        bytesFormatted: String
    ) -> Unit,
) :
    AbstractMediaFilesAdapter(
        context, preloader, true, null,
        toggleCheckCallback
    ) {

    var isProcessing = true

    private var mediaFileListItems: MutableList<ListItem> = mutableListOf()
        set(value) {
            value.clear()
            for (i in mediaFileInfoList.indices) {
                value.add(ListItem(mediaFileInfo = mediaFileInfoList[i]))
                preloader.addItem(mediaFileInfoList[i])
            }
            if (mediaFileInfoList.size != 0) {
                preloader.addItem(null)
                value.add(ListItem(EMPTY_LAST_ITEM))
            }
            field = value
        }

    init {
        mediaFileListItems = mutableListOf()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is MediaInfoRecyclerViewHolder) {
            getMediaFilesListItems()[position].run {
                mediaFileInfo?.let { mediaFileInfo ->
                    if (analysisType == ReviewImagesFragment.TYPE_NETWORK_INTENSIVE_APPS) {
                        val networkBytes = this.mediaFileInfo?.extraInfo?.apkMetaData?.networkBytes
                        if (networkBytes != null) {
                            holder.infoSummary.text = FileUtils.formatStorageLength(
                                context,
                                networkBytes
                            )
                        } else {
                            holder.infoSummary.text = this.mediaFileInfo?.getFormattedSize(context)
                        }
                    } else if (analysisType == ReviewImagesFragment.TYPE_LARGE_SIZE_DIFF_APPS) {
                        val sizeDiff = this.mediaFileInfo?.extraInfo?.apkMetaData?.sizeDiff
                        val totalSize = this.mediaFileInfo?.getFormattedSize(context)
                        if (sizeDiff != null) {
                            val summary =
                                "${context.getString(R.string.size_diff)}: ${
                                    FileUtils
                                        .formatStorageLength(context, sizeDiff)
                                }"
                            val extra = "${context.getString(R.string.size_total)}: $totalSize"
                            // Show the size increase in `infoSummary`
                            holder.infoSummary.text = summary
                            // Show total size in `extraInfo`
                            holder.extraInfo.text = extra
                        } else {
                            holder.infoSummary.text = totalSize
                        }
                    } else if (analysisType == ReviewImagesFragment.TYPE_MOST_USED_APPS) {
                        val timeForeground =
                            Utils.convertMillisToHoursMinutes(
                                this.mediaFileInfo?.extraInfo
                                    ?.apkMetaData?.timeForeground ?: 0L
                            )
                        val size = this.mediaFileInfo?.getFormattedSize(context)
                        "${context.getString(R.string.size)}: $size".also {
                            holder.infoSummary.text = it
                        }
                        holder.extraInfo.text = buildString {
                            append("${context.getString(R.string.usage)}: ")
                            append(timeForeground)
                        }
                    } else {
                        holder.infoSummary.text = this.mediaFileInfo?.getFormattedSize(context)
                    }
                    holder.expand.visibility = View.VISIBLE
                    invalidateCheckedTitle(getOnlyItemsCount())
                    holder.root.setOnClickListener {
                        toggleChecked(this)
                        holder.checkIconGrid.visibility =
                            if (isChecked) View.VISIBLE else View.INVISIBLE
                        invalidateCheckedTitle(getOnlyItemsCount())
                    }
                    holder.expand.setOnClickListener {
                        mediaFileInfo.triggerMediaFileInfoAction(WeakReference(context))
                    }
                }
            }
        } else if (holder is EmptyViewHolder) {
            holder.processingProgressView.invalidateProcessing(
                isProcessing, itemCount == 0,
                context.resources.getString(R.string.analysing)
            )
        }
    }

    /** Returns the number of items. There might be dummy items, that are also counted here */
    override fun getItemCount(): Int {
        return mediaFileListItems.size
    }

    /** Returns the number of actual items. Dummy items are not counted. */
    override fun getOnlyItemsCount(): Int {
        return mediaFileListItems.size - 1
    }

    override fun getItemViewType(position: Int): Int {
        return mediaFileListItems[position].listItemType
    }

    override fun getMediaFilesListItems(): MutableList<ListItem> {
        return mediaFileListItems
    }

    /**
     * for review fragments, adapter underlying list is the main list fetched from analysis, so we
     * can directly delete from that list
     */
    override fun removeChecked(): Boolean {
        val toRemove = getMediaFilesListItems().filter { it.isChecked && it.mediaFileInfo != null }
            .map { it.mediaFileInfo }
        val didRemove = mediaFileInfoList.removeAll(toRemove.toSet())
        return super.removeChecked() && didRemove
    }

}
