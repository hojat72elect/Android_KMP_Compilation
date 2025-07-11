

package com.duckduckgo.app.notification.model

import android.os.Bundle
import androidx.annotation.StringRes

/**
 * This interface is used whenever we want to create a notification that can be scheduled
 * if cancelIntent is null it then uses the default if "com.duckduckgo.notification.cancel"
 * which will cancel the notification and send a pixel
 */
interface SchedulableNotification {
    val id: String
    suspend fun canShow(): Boolean
    suspend fun buildSpecification(): NotificationSpec
}

interface NotificationSpec {
    val channel: Channel
    val systemId: Int
    val name: String
    val icon: Int
    val title: String
    val description: String
    val launchButton: String?
    val closeButton: String?
    val pixelSuffix: String
    val autoCancel: Boolean
    val bundle: Bundle
    val color: Int
}

data class Channel(
    val id: String,
    @StringRes val name: Int,
    val priority: Int,
)
