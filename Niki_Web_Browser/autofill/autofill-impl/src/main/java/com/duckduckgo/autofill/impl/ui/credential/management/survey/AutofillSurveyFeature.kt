

package com.duckduckgo.autofill.impl.ui.credential.management.survey

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.feature.toggles.api.Toggle.InternalAlwaysEnabled
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesRemoteFeature(
    scope = AppScope::class,
    boundType = AutofillSurveysFeature::class,
    featureName = "autofillSurveys",
    settingsStore = AutofillSurveyFeatureSettingsStore::class,
)
/**
 * This is the class that represents the feature flag for showing autofill user-surveys.
 */
interface AutofillSurveysFeature {
    /**
     * @return `true` when the remote config has the global "autofillSurveys" feature flag enabled
     *
     * If the remote feature is not present defaults to `false`
     */

    @InternalAlwaysEnabled
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(AutofillSurveysFeature::class)
class AutofillSurveyFeatureSettingsStore @Inject constructor(
    private val autofillSurveyStore: AutofillSurveyStore,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : FeatureSettings.Store {

    override fun store(jsonString: String) {
        appCoroutineScope.launch(dispatchers.io()) {
            autofillSurveyStore.updateAvailableSurveys(jsonString)
        }
    }
}
