

package com.duckduckgo.duckplayer.api

sealed class PrivatePlayerMode {
    abstract val value: String

    data object Enabled : PrivatePlayerMode() {
        override val value: String = "enabled"
    }

    data object AlwaysAsk : PrivatePlayerMode() {
        override val value: String = "alwaysAsk"
    }

    data object Disabled : PrivatePlayerMode() {
        override val value: String = "disabled"
    }
}
