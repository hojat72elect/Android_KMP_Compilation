

package com.duckduckgo.mobile.android.vpn.service

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationManagerCompat
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.notification.checkPermissionAndNotify
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.service.notification.VpnNotificationStore
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason
import com.duckduckgo.mobile.android.vpn.ui.notification.VpnEnabledNotificationBuilder
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import logcat.logcat

@ContributesMultibinding(VpnScope::class)
@SingleInstanceIn(VpnScope::class)
class VpnTrackerNotificationUpdates @Inject constructor(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val vpnEnabledNotificationContentPluginPoint: PluginPoint<VpnEnabledNotificationContentPlugin>,
    private val vpnNotificationStore: VpnNotificationStore,
) : VpnServiceCallbacks {

    private val systemNotificationManager: NotificationManager? by lazy { context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val job = ConflatedJob()
    private var hasUpdatedInitialState = false
    private var currentPlugin: VpnEnabledNotificationContentPlugin? = null

    override fun onVpnStarted(coroutineScope: CoroutineScope) {
        logcat { "VpnTrackerNotificationUpdates: onVpnStarted called" }
        handleNotificationUpdates(coroutineScope)
    }

    override fun onVpnReconfigured(coroutineScope: CoroutineScope) {
        logcat { "VpnTrackerNotificationUpdates: onVpnReconfigured called" }
        handleNotificationUpdates(coroutineScope)
    }

    override fun onVpnStopped(
        coroutineScope: CoroutineScope,
        vpnStopReason: VpnStopReason,
    ) {
        logcat { "VpnTrackerNotificationUpdates: onVpnStopped called" }
        job.cancel()
        currentPlugin = null
        hasUpdatedInitialState = false
        vpnNotificationStore.persistentNotifDimissedTimestamp = 0L
    }

    private fun handleNotificationUpdates(coroutineScope: CoroutineScope) {
        job += coroutineScope.launch(dispatcherProvider.io()) {
            val newPlugin = vpnEnabledNotificationContentPluginPoint.getHighestPriorityPlugin()

            // If there is a change in plugin, we want to show the updated content
            if (currentPlugin?.uuid != newPlugin?.uuid) {
                currentPlugin = newPlugin
                hasUpdatedInitialState = false
                vpnNotificationStore.persistentNotifDimissedTimestamp = 0L
            }

            currentPlugin?.getUpdatedContent()?.let {
                it.collectLatest { content ->
                    val vpnNotification = content ?: VpnEnabledNotificationContentPlugin.VpnEnabledNotificationContent.EMPTY
                    // We want to update the notification once every state change to be able to update the initial content. Else
                    // we only update if the notification is still active / shown
                    if (!hasUpdatedInitialState || shouldUpdateNotification()) {
                        logcat { "VpnTrackerNotificationUpdates: updating notification" }
                        hasUpdatedInitialState = true
                        updateNotification(vpnNotification)
                        vpnNotificationStore.persistentNotifDimissedTimestamp = 0L
                    } else {
                        logcat { "VpnTrackerNotificationUpdates: Ignoring notification update" }
                    }
                }
            }
        }
    }

    private fun updateNotification(
        vpnNotification: VpnEnabledNotificationContentPlugin.VpnEnabledNotificationContent,
    ) {
        val notification = VpnEnabledNotificationBuilder.buildVpnEnabledUpdateNotification(context, vpnNotification)
        notificationManagerCompat.checkPermissionAndNotify(context, TrackerBlockingVpnService.VPN_FOREGROUND_SERVICE_ID, notification)
    }

    private fun isNotificationVisible(): Boolean {
        val notificationToUpdate =
            systemNotificationManager?.activeNotifications?.filter { it.id == TrackerBlockingVpnService.VPN_FOREGROUND_SERVICE_ID }
        return !notificationToUpdate.isNullOrEmpty()
    }

    private fun hasRequiredTimeElapsed(): Boolean =
        vpnNotificationStore.persistentNotifDimissedTimestamp != 0L &&
            System.currentTimeMillis() - vpnNotificationStore.persistentNotifDimissedTimestamp >= TimeUnit.DAYS.toMillis(1)

    private fun shouldUpdateNotification(): Boolean {
        return !hasUpdatedInitialState || isNotificationVisible() || hasRequiredTimeElapsed()
    }
}
