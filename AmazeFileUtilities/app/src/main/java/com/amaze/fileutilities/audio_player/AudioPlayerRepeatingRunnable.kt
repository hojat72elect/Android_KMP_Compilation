package com.amaze.fileutilities.audio_player

import com.amaze.fileutilities.utilis.AbstractRepeatingRunnable
import java.lang.ref.WeakReference

class AudioPlayerRepeatingRunnable(
    startImmediately: Boolean,
    private val serviceRef: WeakReference<OnPlayerRepeatingCallback>
) :
    AbstractRepeatingRunnable(
        startImmediately
    ) {

    private var lastPosition = 0L

    override fun run() {
        if (serviceRef.get() == null) {
            cancel()
            return
        }
        val callback = serviceRef.get()
        callback?.let {
            it.getAudioProgressHandlerCallback()?.let { audioProgressHandler ->
                if (audioProgressHandler.isCancelled) {
                    it.onProgressUpdate(audioProgressHandler)
                    cancel()
                    return
                }
                val audioPlaybackInfo = audioProgressHandler.audioPlaybackInfo
                audioPlaybackInfo.currentPosition = it.getPlayerPosition()
                audioPlaybackInfo.duration = it.getPlayerDuration()
                audioPlaybackInfo.isPlaying = it.isPlaying()
                if (lastPosition != audioPlaybackInfo.currentPosition) {
                    it.onProgressUpdate(audioProgressHandler)
                }
                lastPosition = it.getPlayerPosition()
            }
        }
    }
}

interface OnPlayerRepeatingCallback {
    fun getAudioProgressHandlerCallback(): AudioProgressHandler?
    fun onProgressUpdate(audioProgressHandler: AudioProgressHandler)
    fun getPlayerPosition(): Long
    fun getPlayerDuration(): Long
    fun isPlaying(): Boolean
}
