package com.amaze.fileutilities.audio_player

import android.content.ComponentName
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import com.amaze.fileutilities.utilis.ObtainableServiceBinder
import com.google.android.exoplayer2.PlaybackParameters
import java.lang.ref.WeakReference

class AudioPlaybackServiceConnection(private val activityRef: WeakReference<OnPlaybackInfoUpdate>) :
    ServiceConnection {

    private var specificService: ServiceOperationCallback? = null
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder: ObtainableServiceBinder<out AudioPlayerService?> =
            service as ObtainableServiceBinder<out AudioPlayerService?>
        specificService = binder.service
        specificService?.let { audioPlayerService ->
            activityRef.get()?.apply {
                audioPlayerService.getPlaybackInfoUpdateCallback(activityRef.get()!!)
                activityRef.get()?.setupActionButtons(WeakReference(audioPlayerService))
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        activityRef.get()?.serviceDisconnected()
        specificService?.getPlaybackInfoUpdateCallback(null)
    }

    fun getAudioServiceInstance(): ServiceOperationCallback? {
        return specificService
    }
}

interface ServiceOperationCallback {
    fun getPlaybackInfoUpdateCallback(onPlaybackInfoUpdate: OnPlaybackInfoUpdate?)
    fun getAudioProgressHandlerCallback(): AudioProgressHandler?
    fun getAudioPlaybackInfo(): AudioPlaybackInfo?
    fun invokePlayPausePlayer()
    fun initLyrics(lyricsText: String, isSynced: Boolean, filePath: String)
    fun clearLyrics()
    fun invokeSeekPlayer(position: Long)
    fun cycleShuffle(): Boolean
    fun cycleRepeat(): Int
    fun getShuffle(): Boolean
    fun getRepeat(): Int
    fun invokePlaybackProperties(playbackSpeed: Float, pitch: Float)
    fun getPlaybackParameters(): PlaybackParameters?
    fun insertPlayNextSong(uri: Uri)
}
