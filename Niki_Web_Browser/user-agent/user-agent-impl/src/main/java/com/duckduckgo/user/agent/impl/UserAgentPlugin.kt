

package com.duckduckgo.user.agent.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.user.agent.store.UserAgentExceptionEntity
import com.duckduckgo.user.agent.store.UserAgentFeatureName
import com.duckduckgo.user.agent.store.UserAgentFeatureToggle
import com.duckduckgo.user.agent.store.UserAgentFeatureToggleRepository
import com.duckduckgo.user.agent.store.UserAgentRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class UserAgentPlugin @Inject constructor(
    private val userAgentRepository: UserAgentRepository,
    private val userAgentFeatureToggleRepository: UserAgentFeatureToggleRepository,
) : PrivacyFeaturePlugin {

    override fun store(
        featureName: String,
        jsonString: String,
    ): Boolean {
        val privacyFeature = userAgentFeatureValueOf(featureName) ?: return false
        if (privacyFeature.value == this.featureName) {
            val userAgentExceptions = mutableListOf<UserAgentExceptionEntity>()
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<UserAgentFeature> =
                moshi.adapter(UserAgentFeature::class.java)

            val userAgentFeature: UserAgentFeature? = jsonAdapter.fromJson(jsonString)
            val exceptionsList = userAgentFeature?.exceptions.orEmpty()

            exceptionsList.forEach {
                userAgentExceptions.add(UserAgentExceptionEntity(it.domain, it.reason.orEmpty()))
            }

            userAgentRepository.updateAll(userAgentExceptions)
            val isEnabled = userAgentFeature?.state == "enabled"
            userAgentFeatureToggleRepository.insert(UserAgentFeatureToggle(this.featureName, isEnabled, userAgentFeature?.minSupportedVersion))
            return true
        }
        return false
    }

    override val featureName: String = UserAgentFeatureName.UserAgent.value
}
