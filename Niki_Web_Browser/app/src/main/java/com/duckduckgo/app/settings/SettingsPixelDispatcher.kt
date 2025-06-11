

package com.duckduckgo.app.settings

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.pixels.AppPixelName.SETTINGS_EMAIL_PROTECTION_PRESSED
import com.duckduckgo.app.pixels.AppPixelName.SETTINGS_SYNC_PRESSED
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.autofill.api.email.EmailManager
import com.duckduckgo.common.utils.extensions.toBinaryString
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckchat.api.DuckChat
import com.duckduckgo.duckchat.impl.pixel.DuckChatPixelName.DUCK_CHAT_SETTINGS_PRESSED
import com.duckduckgo.sync.api.SyncState.OFF
import com.duckduckgo.sync.api.SyncStateMonitor
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Utility to dispatch pixels triggered by interactions on the settings screen.
 */
interface SettingsPixelDispatcher {
    fun fireSyncPressed()
    fun fireDuckChatPressed()
    fun fireEmailPressed()
}

@ContributesBinding(scope = AppScope::class)
@SingleInstanceIn(AppScope::class)
class SettingsPixelDispatcherImpl @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val pixel: Pixel,
    private val syncStateMonitor: SyncStateMonitor,
    private val duckChat: DuckChat,
    private val emailManager: EmailManager,
) : SettingsPixelDispatcher {

    override fun fireSyncPressed() {
        appCoroutineScope.launch {
            val syncState = syncStateMonitor.syncState().firstOrNull()
            val isEnabled = syncState != null && syncState != OFF
            pixel.fire(
                pixel = SETTINGS_SYNC_PRESSED,
                parameters = mapOf(
                    PARAM_SYNC_IS_ENABLED to isEnabled.toBinaryString(),
                ),
            )
        }
    }

    override fun fireDuckChatPressed() {
        appCoroutineScope.launch {
            val wasUsedBefore = duckChat.wasOpenedBefore()
            pixel.fire(
                pixel = DUCK_CHAT_SETTINGS_PRESSED,
                parameters = mapOf(
                    PARAM_DUCK_CHAT_USED_BEFORE to wasUsedBefore.toBinaryString(),
                ),
            )
        }
    }

    override fun fireEmailPressed() {
        appCoroutineScope.launch {
            val isSignedIn = emailManager.isSignedIn()
            pixel.fire(
                pixel = SETTINGS_EMAIL_PROTECTION_PRESSED,
                parameters = mapOf(
                    PARAM_EMAIL_IS_SIGNED_IN to isSignedIn.toBinaryString(),
                ),
            )
        }
    }

    private companion object {
        const val PARAM_SYNC_IS_ENABLED = "is_enabled"
        const val PARAM_DUCK_CHAT_USED_BEFORE = "was_used_before"
        const val PARAM_EMAIL_IS_SIGNED_IN = "is_signed_in"
    }
}
