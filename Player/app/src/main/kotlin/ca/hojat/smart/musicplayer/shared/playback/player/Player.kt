@file:UnstableApi

package ca.hojat.smart.musicplayer.shared.playback.player

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import ca.hojat.smart.musicplayer.shared.extensions.broadcastUpdateWidgetState
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.currentMediaItems
import ca.hojat.smart.musicplayer.shared.extensions.setRepeatMode
import ca.hojat.smart.musicplayer.shared.helpers.SEEK_INTERVAL_MS
import ca.hojat.smart.musicplayer.MainActivity
import ca.hojat.smart.musicplayer.shared.extensions.getCustomLayout
import ca.hojat.smart.musicplayer.shared.playback.PlaybackService
import ca.hojat.smart.musicplayer.shared.playback.PlaybackService.Companion.updatePlaybackInfo
import ca.hojat.smart.musicplayer.shared.playback.SimpleEqualizer

private const val PLAYER_THREAD = "PlayerThread"

/**
 * Initializes player and media session.
 *
 * All player operations are handled on a separate handler thread to avoid slowing down the main thread.
 * See https://developer.android.com/guide/topics/media/exoplayer/hello-world#a-note-on-threading for more info.
 */
internal fun PlaybackService.initializeSessionAndPlayer(
    handleAudioFocus: Boolean,
    handleAudioBecomingNoisy: Boolean
) {
    playerThread = HandlerThread(PLAYER_THREAD).also { it.start() }
    playerHandler = Handler(playerThread.looper)
    player = initializePlayer(handleAudioFocus, handleAudioBecomingNoisy)
    playerListener = getPlayerListener()
    mediaSession =
        MediaLibraryService.MediaLibrarySession.Builder(this, player, getMediaSessionCallback())
            .setSessionActivity(getSessionActivityIntent())
            .build()

    withPlayer {
        addListener(playerListener)
        setRepeatMode(config.playbackSetting)
        setPlaybackSpeed(config.playbackSpeed)
        shuffleModeEnabled = config.isShuffleEnabled
        mediaSession.setCustomLayout(getCustomLayout())
        SimpleEqualizer.setupEqualizer(this@initializeSessionAndPlayer, player)
    }
}

private fun PlaybackService.initializePlayer(
    handleAudioFocus: Boolean,
    handleAudioBecomingNoisy: Boolean
): SimpleMusicPlayer {
    val renderersFactory = AudioOnlyRenderersFactory(context = this)
    return SimpleMusicPlayer(
        ExoPlayer.Builder(this, renderersFactory)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .setHandleAudioBecomingNoisy(handleAudioBecomingNoisy)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                handleAudioFocus
            )
            .setSkipSilenceEnabled(
                // TODO: Enable when https://github.com/androidx/media/issues/712 is resolved.
                //  See https://github.com/SimpleMobileTools/Simple-Music-Player/issues/604
                false //skipSilence
            )
            .setSeekBackIncrementMs(SEEK_INTERVAL_MS)
            .setSeekForwardIncrementMs(SEEK_INTERVAL_MS)
            .setLooper(playerThread.looper)
            .build()
    )
}

private fun Context.getSessionActivityIntent(): PendingIntent {
    return PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}

internal fun PlaybackService.updatePlaybackState() {
    withPlayer {
        updatePlaybackInfo(player)
        broadcastUpdateWidgetState()
        val currentMediaItem = currentMediaItem
        if (currentMediaItem != null) {
            mediaItemProvider.saveRecentItemsWithStartPosition(
                mediaItems = currentMediaItems,
                current = currentMediaItem,
                startPosition = currentPosition
            )
        }
    }
}
