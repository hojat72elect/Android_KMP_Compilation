package com.amaze.fileutilities.video_player

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R
import com.amaze.fileutilities.utilis.showToastInCenter

class SubtitlesSearchResultsAdapter(
    private val appContext: Context,
    private val listState: List<SubtitleResult>,
    private val downloadFileCallback: (String?, String?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = mInflater.inflate(
            R.layout.subtitles_result_row_item, parent,
            false
        )
        return SubtitlesSearchResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SubtitlesSearchResultsViewHolder) {
            val subtitleResult = listState[position]
            holder.movieName.text = subtitleResult.title
            holder.info.text =
                buildString {
                    append("${appContext.resources.getString(R.string.cd)}: ")
                    append("${subtitleResult.cdNumber ?: ""} | ")
                    append("${appContext.resources.getString(R.string.upload_date)}: ")
                    append("${subtitleResult.uploadDate?.replace("\n", "")} | ")
                    append("${appContext.resources.getString(R.string.rating)}: ")
                    append("${subtitleResult.subtitleRating ?: ""} | ")
                    append("${appContext.resources.getString(R.string.language)}: ")
                    append("${subtitleResult.language ?: ""} | ")
                    append("${appContext.resources.getString(R.string.uploader)}:")
                    append(" ${subtitleResult.uploader ?: ""} | ")
                    append("${appContext.resources.getString(R.string.downloads)}: ")
                    append(subtitleResult.downloads ?: "")
                }
            holder.parentLayout.setOnClickListener {
                if (subtitleResult.downloadId == null) {
                    appContext.showToastInCenter(
                        appContext.resources
                            .getString(R.string.choose_different_subtitle)
                    )
                } else {
                    downloadFileCallback.invoke(
                        subtitleResult.downloadId,
                        subtitleResult.downloadFileName
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listState.size
    }

    private val mInflater: LayoutInflater
        get() = appContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     * downloadLink - in format - /en/subtitleserve/sub/5136695
     */
    data class SubtitleResult(
        var title: String? = null,
        var language: String? = null,
        var cdNumber: String? = null,
        var uploadDate: String? = null,
        var downloadId: String? = null,
        var subtitleRating: String? = null,
        var downloads: String? = null,
        var uploader: String? = null,
        var downloadFileName: String? = null,
    )

    class SubtitlesSearchResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @JvmField
        val parentLayout: RelativeLayout = view.findViewById(R.id.row_layout_parent)

        @JvmField
        val movieName: TextView = view.findViewById(R.id.movie_name)

        @JvmField
        val info: TextView = view.findViewById(R.id.info)
    }
}
