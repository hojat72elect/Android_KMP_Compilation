

package com.duckduckgo.app.dev.settings.notifications

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.browser.databinding.ActivityNotificationsBinding
import com.duckduckgo.app.dev.settings.notifications.NotificationViewModel.Command.TriggerNotification
import com.duckduckgo.app.dev.settings.notifications.NotificationViewModel.ViewState
import com.duckduckgo.app.notification.NotificationFactory
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.view.listitem.TwoLineListItem
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.notification.checkPermissionAndNotify
import com.duckduckgo.di.scopes.ActivityScope
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InjectWith(ActivityScope::class)
class NotificationsActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var viewModel: NotificationViewModel

    @Inject
    lateinit var factory: NotificationFactory

    private val binding: ActivityNotificationsBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.includeToolbar.toolbar)

        observeViewState()
        observeCommands()
    }

    private fun observeViewState() {
        viewModel.viewState.flowWithLifecycle(lifecycle, STARTED).onEach { render(it) }
            .launchIn(lifecycleScope)
    }

    private fun observeCommands() {
        viewModel.command.flowWithLifecycle(lifecycle, STARTED).onEach { command ->
            when (command) {
                is TriggerNotification -> addNotification(id = command.notificationItem.id, notification = command.notificationItem.notification)
            }
        }.launchIn(lifecycleScope)
    }

    private fun render(viewState: ViewState) {
        viewState.scheduledNotifications.forEach { notificationItem ->
            buildNotificationItem(
                title = notificationItem.title,
                subtitle = notificationItem.subtitle,
                onClick = { viewModel.onNotificationItemClick(notificationItem) },
            ).also {
                binding.scheduledNotificationsContainer.addView(it)
            }
        }

        viewState.vpnNotifications.forEach { notificationItem ->
            buildNotificationItem(
                title = notificationItem.title,
                subtitle = notificationItem.subtitle,
                onClick = { viewModel.onNotificationItemClick(notificationItem) },
            ).also {
                binding.vpnNotificationsContainer.addView(it)
            }
        }
    }

    private fun buildNotificationItem(
        title: String,
        subtitle: String,
        onClick: () -> Unit,
    ): TwoLineListItem {
        return TwoLineListItem(this).apply {
            setPrimaryText(title)
            setSecondaryText(subtitle)
            setOnClickListener { onClick() }
        }
    }

    private fun addNotification(
        id: Int,
        notification: Notification,
    ) {
        NotificationManagerCompat.from(this)
            .checkPermissionAndNotify(context = this, id = id, notification = notification)
    }

    companion object {

        fun intent(context: Context): Intent {
            return Intent(context, NotificationsActivity::class.java)
        }
    }
}
