

package com.duckduckgo.app.notification

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.notification.model.SchedulableNotification
import com.duckduckgo.app.notification.model.SchedulableNotificationPlugin
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.ServiceScope
import dagger.android.AndroidInjection
import javax.inject.Inject

@InjectWith(ServiceScope::class)
class NotificationHandlerService : IntentService("NotificationHandlerService") {

    @Inject
    lateinit var schedulableNotificationPluginPoint: PluginPoint<SchedulableNotificationPlugin>

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    @VisibleForTesting
    public override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val notificationJavaClass = intent.type ?: return
        val notificationPlugin = schedulableNotificationPluginPoint.getPlugins().firstOrNull {
            notificationJavaClass == it.getSchedulableNotification().javaClass.simpleName
        }
        notificationPlugin?.onNotificationCancelled()
    }

    companion object {
        fun pendingCancelNotificationHandlerIntent(
            context: Context,
            notificationJavaClass: Class<SchedulableNotification>,
        ): PendingIntent {
            val intent = Intent(context, NotificationHandlerService::class.java)
            intent.type = notificationJavaClass.simpleName
            return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)!!
        }
    }
}
