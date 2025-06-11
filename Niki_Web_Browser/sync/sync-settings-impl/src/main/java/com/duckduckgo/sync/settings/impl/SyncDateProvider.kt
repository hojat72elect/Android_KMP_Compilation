

package com.duckduckgo.sync.settings.impl

import java.time.*
import java.time.format.*

class SyncDateProvider {
    companion object {
        fun now(): String = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
    }
}
