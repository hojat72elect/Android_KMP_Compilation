

package com.duckduckgo.autofill.impl.reporting.remoteconfig

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue

@ContributesRemoteFeature(
    scope = AppScope::class,
    boundType = AutofillSiteBreakageReportingFeature::class,
    featureName = "autofillBreakageReporter",
    settingsStore = AutofillSiteBreakageReportingRemoteSettingsPersister::class,
)
/**
 * This is the class that represents the feature flag for offering to report Autofill breakages
 */
interface AutofillSiteBreakageReportingFeature {
    /**
     * @return `true` when the remote config has the global "autofillBreakageReporter" feature flag enabled, and always true for internal builds
     *
     * If the remote feature is not present defaults to `false`
     */

    @Toggle.InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}
