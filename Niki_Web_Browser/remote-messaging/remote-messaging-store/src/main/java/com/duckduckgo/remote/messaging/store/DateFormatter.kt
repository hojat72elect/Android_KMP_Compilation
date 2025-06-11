

package com.duckduckgo.remote.messaging.store

import java.time.format.DateTimeFormatter

internal fun databaseTimestampFormatter() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
