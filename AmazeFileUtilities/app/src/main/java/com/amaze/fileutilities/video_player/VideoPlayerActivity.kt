package com.amaze.fileutilities.video_player

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.amaze.fileutilities.R

class VideoPlayerActivity : BaseVideoPlayerActivity() {

    companion object {
        const val VIDEO_PLAYBACK_POSITION = "playback_position"
    }

    override fun isDialogActivity(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                setTheme(R.style.Theme_AmazeFileUtilities_FullScreen_Dark)
            } catch (e: Exception) {
                log.warn("failed to set theme Theme_AmazeFileUtilities_FullScreen_Dark", e)
                setTheme(R.style.Theme_AmazeFileUtilities_FullScreen_Dark_Fallback)
            }
        }
        initLocalVideoModel(intent)
        super.onCreate(savedInstanceState)
        handleVideoPlayerActivityResources()
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // allow to go in notch area in landscape mode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Set the system UI visibility flags
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                // Get the WindowInsetsController
                val controller = window.insetsController

                // Hide the system bars (navigation bar, status bar)
                controller?.hide(WindowInsets.Type.systemBars())

                // Enable the extended layout to be displayed in the notch area
                controller?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
    }
}
