

package com.duckduckgo.pir.internal.store.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "pir_broker_json_etag")
data class BrokerJsonEtag(
    @PrimaryKey val fileName: String,
    val etag: String,
    val isActive: Boolean,
)

@Entity(tableName = "pir_broker_details")
data class Broker(
    @PrimaryKey val name: String,
    val fileName: String,
    val url: String,
    val version: String,
    val parent: String?,
    val addedDatetime: Long,
)

@Entity(
    tableName = "pir_broker_scan",
    foreignKeys = [
        ForeignKey(
            entity = Broker::class,
            parentColumns = ["name"],
            childColumns = ["brokerName"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class BrokerScan(
    @PrimaryKey val brokerName: String,
    val stepsJson: String?,
)

@Entity(
    tableName = "pir_broker_opt_out",
    foreignKeys = [
        ForeignKey(
            entity = Broker::class,
            parentColumns = ["name"],
            childColumns = ["brokerName"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class BrokerOptOut(
    @PrimaryKey val brokerName: String,
    val stepsJson: String,
    val optOutUrl: String?,
)

@Entity(
    tableName = "pir_broker_scheduling_config",
    foreignKeys = [
        ForeignKey(
            entity = Broker::class,
            parentColumns = ["name"],
            childColumns = ["brokerName"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class BrokerSchedulingConfig(
    @PrimaryKey val brokerName: String,
    val retryError: Int,
    val confirmOptOutScan: Int,
    val maintenanceScan: Int,
    val maxAttempts: Int?,
)
