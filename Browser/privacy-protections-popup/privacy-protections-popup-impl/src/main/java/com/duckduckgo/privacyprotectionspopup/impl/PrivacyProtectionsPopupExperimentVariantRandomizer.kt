

package com.duckduckgo.privacyprotectionspopup.impl

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.privacyprotectionspopup.impl.PrivacyProtectionsPopupExperimentVariant.CONTROL
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import org.apache.commons.math3.random.RandomDataGenerator

interface PrivacyProtectionsPopupExperimentVariantRandomizer {
    fun getRandomVariant(): PrivacyProtectionsPopupExperimentVariant
}

@ContributesBinding(FragmentScope::class)
class PrivacyProtectionsPopupExperimentVariantRandomizerImpl @Inject constructor(
    private val buildConfig: AppBuildConfig,
) : PrivacyProtectionsPopupExperimentVariantRandomizer {

    override fun getRandomVariant(): PrivacyProtectionsPopupExperimentVariant {
        if (buildConfig.isDefaultVariantForced) return CONTROL

        val variants = PrivacyProtectionsPopupExperimentVariant.entries
        val randomIndex = RandomDataGenerator().nextInt(0, variants.lastIndex)
        return variants[randomIndex]
    }
}
