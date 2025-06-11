

package com.duckduckgo.pir.internal.store.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Contains the brokers + extracted profiles where the opt-out flow has been completed.
 * A broker can have multiple extracted profiles and for each broker + profile combination,
 * we need to complete an opt out flow for.
 */
@Entity(tableName = "pir_opt_out_complete_brokers")
data class OptOutCompletedBroker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brokerName: String,
    val extractedProfile: String,
    val startTimeInMillis: Long,
    val endTimeInMillis: Long,
    val isSubmitSuccess: Boolean,
)

/**
 * This table contains ALL results from any action during the opt-out flow.
 * This is mostly for logging purpose.
 */
@Entity(tableName = "pir_opt_out_action_log")
data class OptOutActionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brokerName: String,
    val extractedProfile: String,
    val completionTimeInMillis: Long,
    val actionType: String,
    val isError: Boolean,
    val result: String,
)
