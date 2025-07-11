

package com.duckduckgo.autofill.impl.service

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.InternalAlwaysEnabled

@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "autofillService",
)
interface AutofillServiceFeature {

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @InternalAlwaysEnabled
    fun self(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @InternalAlwaysEnabled
    fun canUpdateAppToDomainDataset(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @InternalAlwaysEnabled
    fun canMapAppToDomain(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    @InternalAlwaysEnabled
    fun canAutofillInsideDDG(): Toggle

    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun canProcessSystemFillRequests(): Toggle
}
