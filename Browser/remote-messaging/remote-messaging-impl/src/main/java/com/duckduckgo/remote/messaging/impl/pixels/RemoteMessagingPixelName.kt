

package com.duckduckgo.remote.messaging.impl.pixels

import com.duckduckgo.app.statistics.pixels.Pixel

enum class RemoteMessagingPixelName(override val pixelName: String) : Pixel.PixelName {

    REMOTE_MESSAGE_DISMISSED("m_remote_message_dismissed"),
    REMOTE_MESSAGE_SHOWN("m_remote_message_shown"),
    REMOTE_MESSAGE_SHOWN_UNIQUE("m_remote_message_shown_unique"),
    REMOTE_MESSAGE_PRIMARY_ACTION_CLICKED("m_remote_message_primary_action_clicked"),
    REMOTE_MESSAGE_SECONDARY_ACTION_CLICKED("m_remote_message_secondary_action_clicked"),
    REMOTE_MESSAGE_ACTION_CLICKED("m_remote_message_action_clicked"),
    REMOTE_MESSAGE_SHARED("m_remote_message_share"),
}
