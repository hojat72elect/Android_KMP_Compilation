

package com.duckduckgo.pir.internal.store.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pir_events_log")
data class PirEventLog(
    @PrimaryKey val eventTimeInMillis: Long,
    val eventType: EventType,
)

enum class EventType {
    MANUAL_SCAN_STARTED,
    MANUAL_SCAN_COMPLETED,
    SCHEDULED_SCAN_SCHEDULED,
    SCHEDULED_SCAN_STARTED,
    SCHEDULED_SCAN_COMPLETED,
    MANUAL_OPTOUT_STARTED,
    MANUAL_OPTOUT_COMPLETED,
}

@Entity(tableName = "pir_broker_scan_log")
data class PirBrokerScanLog(
    @PrimaryKey val eventTimeInMillis: Long,
    val brokerName: String,
    val eventType: BrokerScanEventType,
)

enum class BrokerScanEventType {
    BROKER_STARTED,
    BROKER_SUCCESS,
    BROKER_ERROR,
}
