package com.amaze.fileutilities.video_player

import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.RelativeLayout
import com.amaze.fileutilities.R
import com.amaze.fileutilities.utilis.px
import com.google.android.exoplayer2.ui.PlayerView

class VideoPlayerDialogActivity : BaseVideoPlayerActivity() {

    override fun isDialogActivity(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleViewPlayerDialogActivityResources()
        findViewById<RelativeLayout>(R.id.video_parent).setPadding(
            16.px.toInt(), 16.px.toInt(),
            16.px.toInt(), 16.px.toInt()
        )

        val playerView = findViewById<PlayerView>(R.id.video_view)
        playerView.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                view?.let { view ->
                    outline?.setRoundRect(0, 0, view.width, view.height, 24.px)
                }
            }
        }
        playerView.clipToOutline = true
    }
}
