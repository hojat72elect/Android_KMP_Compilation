package ca.hojat.smart.musicplayer.shared.playback

import android.os.Bundle
import android.os.ConditionVariable
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.core.os.postDelayed
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.SettableFuture
import ca.hojat.smart.musicplayer.R
import ca.hojat.smart.musicplayer.shared.data.models.Events
import ca.hojat.smart.musicplayer.shared.extensions.addRemainingMediaItems
import ca.hojat.smart.musicplayer.shared.extensions.config
import ca.hojat.smart.musicplayer.shared.extensions.getCustomLayout
import ca.hojat.smart.musicplayer.shared.extensions.getPlaybackSetting
import ca.hojat.smart.musicplayer.shared.extensions.hasPermission
import ca.hojat.smart.musicplayer.shared.extensions.isReallyPlaying
import ca.hojat.smart.musicplayer.shared.extensions.nextMediaItem
import ca.hojat.smart.musicplayer.shared.helpers.EXTRA_NEXT_MEDIA_ID
import ca.hojat.smart.musicplayer.shared.helpers.EXTRA_SHUFFLE_INDICES
import ca.hojat.smart.musicplayer.shared.helpers.NotificationHelper
import ca.hojat.smart.musicplayer.shared.helpers.PlaybackSetting
import ca.hojat.smart.musicplayer.shared.helpers.getPermissionToRequest
import ca.hojat.smart.musicplayer.shared.playback.library.MediaItemProvider
import ca.hojat.smart.musicplayer.shared.playback.player.SimpleMusicPlayer
import ca.hojat.smart.musicplayer.shared.playback.player.initializeSessionAndPlayer
import ca.hojat.smart.musicplayer.shared.playback.player.updatePlaybackState
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase
import ca.hojat.smart.musicplayer.shared.usecases.ShowToastUseCase.invoke
import java.util.concurrent.Executors
import org.greenrobot.eventbus.EventBus

@OptIn(UnstableApi::class)
class PlaybackService : MediaLibraryService(), MediaSessionService.Listener {

    private var isActive = false
    private var sleepTimer: CountDownTimer? = null
    lateinit var player: SimpleMusicPlayer
    lateinit var playerThread: HandlerThread
    lateinit var playerListener: Player.Listener
    lateinit var playerHandler: Handler
    lateinit var mediaSession: MediaLibrarySession
    lateinit var mediaItemProvider: MediaItemProvider
    private val customCommands = CustomCommands.entries.map { it.sessionCommand }
    var currentRoot = ""

    override fun onCreate() {
        super.onCreate()
        setListener(this)
        initializeSessionAndPlayer(
            handleAudioFocus = true,
            handleAudioBecomingNoisy = true
        )
        initializeLibrary()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaSession()
        clearListener()
        stopSleepTimer()
        SimpleEqualizer.release()
    }

    fun stopService() {
        withPlayer {
            pause()
            stop()
        }

        stopSelf()
    }

    private fun initializeLibrary() {
        mediaItemProvider = MediaItemProvider(this)
        if (hasPermission(getPermissionToRequest())) {
            mediaItemProvider.reload()
        } else {
            showNoPermissionNotification()
        }
    }

    private fun releaseMediaSession() {
        mediaSession.release()
        withPlayer {
            removeListener(playerListener)
            release()
        }
    }

    fun withPlayer(callback: SimpleMusicPlayer.() -> Unit) =
        playerHandler.post { callback(player) }

    private fun showNoPermissionNotification() {
        Handler(Looper.getMainLooper()).postDelayed(delayInMillis = 100L) {
            try {
                startForeground(
                    NotificationHelper.NOTIFICATION_ID,
                    NotificationHelper.createInstance(this).createNoPermissionNotification()
                )
            } catch (ignored: Exception) {
            }
        }
    }

    /**
     * This method is only required to be implemented on Android 12 or above when an attempt is made
     * by a media controller to resume playback when the {@link MediaSessionService} is in the
     * background.
     */
    override fun onForegroundServiceStartNotAllowedException() {
        ShowToastUseCase(this, "The error : ${getString(R.string.unknown_error_occurred)}")
        // todo: show a notification instead.
    }

    fun toggleSleepTimer() {
        if (isActive) {
            stopSleepTimer()
        } else {
            startSleepTimer()
        }
    }

    private fun startSleepTimer() {
        val millisInFuture = config.sleepInTS - System.currentTimeMillis() + 1000L
        sleepTimer?.cancel()
        sleepTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                EventBus.getDefault().post(Events.SleepTimerChanged(seconds))
            }

            override fun onFinish() {
                config.sleepInTS = 0
                EventBus.getDefault().post(Events.SleepTimerChanged(0))
                stopSleepTimer()
                stopService()
            }
        }

        sleepTimer?.start()
        isActive = true
    }

    private fun stopSleepTimer() {
        sleepTimer?.cancel()
        sleepTimer = null
        isActive = false
        config.sleepInTS = 0
    }

    @UnstableApi
    internal fun getMediaSessionCallback() = object : MediaLibrarySession.Callback {
        private val browsers = mutableMapOf<MediaSession.ControllerInfo, String>()
        private val executorService by lazy {
            MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(4))
        }

        private fun <T> callWhenSourceReady(action: () -> T): ListenableFuture<T> {
            val conditionVariable = ConditionVariable()
            return if (mediaItemProvider.whenReady { conditionVariable.open() }) {
                executorService.submit<T> {
                    action()
                }
            } else {
                executorService.submit<T> {
                    conditionVariable.block()
                    action()
                }
            }
        }

        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val connectionResult = super.onConnect(session, controller)
            val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
            for (command in customCommands) {
                availableSessionCommands.add(command)
            }

            return MediaSession.ConnectionResult.accept(
                availableSessionCommands.build(),
                connectionResult.availablePlayerCommands
            )
        }

        override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
            val customLayout = getCustomLayout()
            if (customLayout.isNotEmpty() && controller.controllerVersion != 0) {
                mediaSession.setCustomLayout(controller, customLayout)
            }
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            val command = CustomCommands.fromSessionCommand(customCommand)
                ?: return Futures.immediateFuture(SessionResult(SessionResult.RESULT_ERROR_BAD_VALUE))

            when (command) {
                CustomCommands.CLOSE_PLAYER -> stopService()
                CustomCommands.RELOAD_CONTENT -> reloadContent()
                CustomCommands.TOGGLE_SLEEP_TIMER -> toggleSleepTimer()
                CustomCommands.TOGGLE_SKIP_SILENCE -> player.setSkipSilence(config.gaplessPlayback)
                CustomCommands.SET_SHUFFLE_ORDER -> setShuffleOrder(args)
                CustomCommands.SET_NEXT_ITEM -> setNextItem(args)
            }

            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }

        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> {
            if (params != null && params.isRecent) {
                // The service currently does not support recent playback. Tell System UI by returning
                // an error of type 'RESULT_ERROR_NOT_SUPPORTED' for a `params.isRecent` request. See
                // https://github.com/androidx/media/issues/355
                return Futures.immediateFuture(LibraryResult.ofError(LibraryResult.RESULT_ERROR_NOT_SUPPORTED))
            }

            return Futures.immediateFuture(
                LibraryResult.ofItem(
                    mediaItemProvider.getRootItem(),
                    params
                )
            )
        }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ) = callWhenSourceReady {
            currentRoot = parentId
            val children = mediaItemProvider.getChildren(parentId)
                ?: return@callWhenSourceReady LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)

            LibraryResult.ofItemList(children, params)
        }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String
        ) = callWhenSourceReady {
            val item = mediaItemProvider[mediaId]
                ?: return@callWhenSourceReady LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)

            LibraryResult.ofItem(item, null)
        }

        override fun onSubscribe(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            params: LibraryParams?
        ) = callWhenSourceReady {
            val children = mediaItemProvider.getChildren(parentId)
                ?: return@callWhenSourceReady LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)

            browsers[browser] = parentId
            session.notifyChildrenChanged(browser, parentId, children.size, params)
            LibraryResult.ofVoid()
        }

        override fun onPlaybackResumption(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            val settableFuture = SettableFuture.create<MediaSession.MediaItemsWithStartPosition>()
            executorService.execute {
                var futureSet = false
                mediaItemProvider.getRecentItemsLazily {
                    // resume playback as quickly as possible: https://github.com/androidx/media/issues/111
                    if (!futureSet) {
                        settableFuture.set(it)
                        futureSet = true
                    } else {
                        player.addRemainingMediaItems(it.mediaItems, it.startIndex)
                    }
                }
            }

            return settableFuture
        }

        override fun onSetMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
            startIndex: Int,
            startPositionMs: Long
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            if (controller.packageName == packageName) {
                return super.onSetMediaItems(
                    mediaSession,
                    controller,
                    mediaItems,
                    startIndex,
                    startPositionMs
                )
            }

            // this is to avoid single items in the queue: https://github.com/androidx/media/issues/156
            var queueItems = mediaItems
            val startItemId = mediaItems[0].mediaId
            val currentItems = mediaItemProvider.getChildren(currentRoot).orEmpty()

            queueItems = if (currentItems.any { it.mediaId == startItemId }) {
                currentItems.toMutableList()
            } else {
                mediaItemProvider.getDefaultQueue()?.toMutableList() ?: queueItems
            }

            val startItemIndex = queueItems.indexOfFirst { it.mediaId == startItemId }
            return super.onSetMediaItems(
                mediaSession,
                controller,
                queueItems,
                startItemIndex,
                startPositionMs
            )
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: List<MediaItem>
        ): ListenableFuture<List<MediaItem>> {
            val items = mediaItems.map { mediaItem ->
                if (mediaItem.requestMetadata.searchQuery != null) {
                    getMediaItemFromSearchQuery(mediaItem.requestMetadata.searchQuery!!)
                } else {
                    mediaItemProvider[mediaItem.mediaId] ?: mediaItem
                }
            }

            return Futures.immediateFuture(items)
        }

        private fun getMediaItemFromSearchQuery(query: String): MediaItem {
            return mediaItemProvider.getItemFromSearch(query.lowercase())
                ?: mediaItemProvider.getRandomItem()
        }

        private fun reloadContent() {
            mediaItemProvider.reload()
            mediaItemProvider.whenReady {
                val rootItem = mediaItemProvider.getRootItem()
                val rootItemCount = mediaItemProvider.getChildren(rootItem.mediaId)?.size ?: 0

                executorService.execute {
                    browsers.forEach { (browser, parentId) ->
                        val itemCount = mediaItemProvider.getChildren(parentId)?.size ?: 0
                        mediaSession.notifyChildrenChanged(browser, parentId, itemCount, null)
                        mediaSession.notifyChildrenChanged(
                            browser,
                            rootItem.mediaId,
                            rootItemCount,
                            null
                        )
                    }
                }
            }
        }

        private fun setShuffleOrder(args: Bundle) {
            val indices = args.getIntArray(EXTRA_SHUFFLE_INDICES) ?: return
            withPlayer {
                setShuffleIndices(indices)
            }
        }

        private fun setNextItem(args: Bundle) {
            val mediaId = args.getString(EXTRA_NEXT_MEDIA_ID) ?: return
            callWhenSourceReady {
                val mediaItem = mediaItemProvider[mediaId] ?: return@callWhenSourceReady
                withPlayer {
                    setNextMediaItem(mediaItem)
                    updatePlaybackState()
                }
            }
        }
    }

    @UnstableApi
    internal fun getPlayerListener() = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) = ShowToastUseCase(this@PlaybackService , R.string.unknown_error_occurred, Toast.LENGTH_LONG)

        override fun onEvents(player: Player, events: Player.Events) {
            if (
                events.containsAny(
                    Player.EVENT_POSITION_DISCONTINUITY,
                    Player.EVENT_MEDIA_ITEM_TRANSITION,
                    Player.EVENT_TRACKS_CHANGED,
                    Player.EVENT_TIMELINE_CHANGED,
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_IS_PLAYING_CHANGED,
                    Player.EVENT_PLAYLIST_METADATA_CHANGED
                )
            ) {
                updatePlaybackState()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            // customize repeat mode behaviour as the default behaviour doesn't align with our requirements.
            withPlayer {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT) {
                    if (config.playbackSetting == PlaybackSetting.STOP_AFTER_CURRENT_TRACK) {
                        seekTo(0)
                        pause()
                    }
                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            if (config.playbackSetting != PlaybackSetting.STOP_AFTER_CURRENT_TRACK) {
                config.playbackSetting = getPlaybackSetting(repeatMode)
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            config.isShuffleEnabled = shuffleModeEnabled
        }
    }

    companion object {
        // Initializing a media controller might take a noticeable amount of time thus we expose current playback info here to keep things as quick as possible.
        var isPlaying: Boolean = false
            private set
        var currentMediaItem: MediaItem? = null
            private set
        var nextMediaItem: MediaItem? = null
            private set

        fun updatePlaybackInfo(player: Player) {
            currentMediaItem = player.currentMediaItem
            nextMediaItem = player.nextMediaItem
            isPlaying = player.isReallyPlaying
        }
    }
}

