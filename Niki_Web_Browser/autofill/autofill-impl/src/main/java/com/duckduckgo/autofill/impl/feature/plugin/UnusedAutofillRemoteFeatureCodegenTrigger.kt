

package com.duckduckgo.autofill.impl.feature.plugin

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.di.scopes.AppScope

@ContributesRemoteFeature(
    scope = AppScope::class,
    boundType = AutofillFeature::class,
    featureName = "autofill",
)
@Suppress("unused")
private interface UnusedAutofillRemoteFeatureCodegenTrigger
