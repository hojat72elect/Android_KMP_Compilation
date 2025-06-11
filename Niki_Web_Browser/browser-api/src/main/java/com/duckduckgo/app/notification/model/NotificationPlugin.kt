

package com.duckduckgo.app.notification.model

interface NotificationPlugin {
    fun getChannels(): List<Channel>
}
