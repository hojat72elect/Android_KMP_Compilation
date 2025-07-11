

package com.duckduckgo.autofill.impl.reporting.remoteconfig

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autofill.impl.reporting.AutofillSiteBreakageReportingDataStore
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(AutofillSiteBreakageReportingFeature::class)
class AutofillSiteBreakageReportingRemoteSettingsPersister @Inject constructor(
    private val dataStore: AutofillSiteBreakageReportingDataStore,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : FeatureSettings.Store {

    override fun store(jsonString: String) {
        appCoroutineScope.launch(dispatchers.io()) {
            val json = JSONObject(jsonString)

            "monitorIntervalDays".let {
                if (json.has(it)) {
                    dataStore.updateMinimumNumberOfDaysBeforeReportPromptReshown(json.getInt(it))
                }
            }
        }
    }
}
