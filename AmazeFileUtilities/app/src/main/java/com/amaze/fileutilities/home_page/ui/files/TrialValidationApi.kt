package com.amaze.fileutilities.home_page.ui.files

import androidx.annotation.Keep
import com.amaze.fileutilities.BuildConfig
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TrialValidationApi {
    companion object {
        const val CLOUD_FUNCTION_BASE = BuildConfig.BASE_CLOUD_FUNC
        const val AUTH_TOKEN = BuildConfig.API_REQ_TRIAL_AUTH_TOKEN
    }

    @POST(BuildConfig.API_REQ_TRIAL_URI)
    fun postValidation(
        @Body trialRequest: TrialRequest
    ): Call<TrialResponse>?

    @Keep
    data class TrialRequest(
        val token: String,
        val deviceId: String,
        val appHash: String,
        var subscriptionStatus: Int,
        var purchaseToken: String?,
        val isPurchaseInApp: Boolean = false,
    )

    @Keep
    data class TrialResponse(
        val isLastDay: Boolean = false,
        val isNewSignup: Boolean = false,
        val trialStatus: Int,
        val trialDaysLeft: Int = 0,
        val subscriptionStatus: Int,
        val purchaseToken: String?,
        var isNotConnected: Boolean = false
    ) {

        companion object {
            const val TRIAL_ACTIVE = BuildConfig.API_REQ_TRIAL_TRIAL_ACTIVE
            const val TRIAL_EXPIRED = BuildConfig.API_REQ_TRIAL_TRIAL_EXPIRED
            const val TRIAL_INACTIVE = BuildConfig.API_REQ_TRIAL_TRIAL_INACTIVE
            const val TRIAL_EXCLUSIVE = BuildConfig.API_REQ_TRIAL_TRIAL_EXCLUSIVE
            const val TRIAL_UNOFFICIAL = BuildConfig.API_REQ_TRIAL_TRIAL_UNOFFICIAL
            const val SUBSCRIPTION = BuildConfig.API_REQ_TRIAL_YEARLY
            val CODE_TRIAL_ACTIVE: Int = BuildConfig.API_REQ_TRIAL_CODE_TRIAL_ACTIVE
            private val CODE_TRIAL_EXPIRED: Int = BuildConfig.API_REQ_TRIAL_CODE_TRIAL_EXPIRED
            private val CODE_TRIAL_INACTIVE: Int = BuildConfig.API_REQ_TRIAL_CODE_TRIAL_INACTIVE
            private val CODE_TRIAL_EXCLUSIVE: Int = BuildConfig.API_REQ_TRIAL_CODE_TRIAL_EXCLUSIVE
            private val CODE_TRIAL_UNOFFICIAL: Int = BuildConfig.API_REQ_TRIAL_CODE_TRIAL_UNOFFICIAL

            val trialStatusMap = mapOf(
                Pair(TRIAL_ACTIVE, "Trial"),
                Pair(TRIAL_EXPIRED, "Trial Expired"),
                Pair(TRIAL_INACTIVE, "Inactive"),
                Pair(TRIAL_EXCLUSIVE, "Lifetime"),
                Pair(TRIAL_UNOFFICIAL, "Unofficial")
            )

            private val trialStatusCodeMap = mapOf(
                Pair(CODE_TRIAL_ACTIVE, TRIAL_ACTIVE),
                Pair(CODE_TRIAL_EXPIRED, TRIAL_EXPIRED),
                Pair(CODE_TRIAL_INACTIVE, TRIAL_INACTIVE),
                Pair(CODE_TRIAL_EXCLUSIVE, TRIAL_EXCLUSIVE),
                Pair(CODE_TRIAL_UNOFFICIAL, TRIAL_UNOFFICIAL)
            )

            val trialCodeStatusMap = mapOf(
                Pair(TRIAL_ACTIVE, CODE_TRIAL_ACTIVE),
                Pair(TRIAL_EXPIRED, CODE_TRIAL_EXPIRED),
                Pair(TRIAL_INACTIVE, CODE_TRIAL_INACTIVE),
                Pair(TRIAL_EXCLUSIVE, CODE_TRIAL_EXCLUSIVE),
                Pair(TRIAL_UNOFFICIAL, CODE_TRIAL_UNOFFICIAL)
            )
        }

        fun getTrialStatus(): String {
            val statusCode = getTrialStatusCode()
            return trialStatusMap[statusCode] ?: SUBSCRIPTION
        }

        fun getTrialStatusCode(): String {
            return trialStatusCodeMap[trialStatus] ?: TRIAL_ACTIVE
        }
    }
}
