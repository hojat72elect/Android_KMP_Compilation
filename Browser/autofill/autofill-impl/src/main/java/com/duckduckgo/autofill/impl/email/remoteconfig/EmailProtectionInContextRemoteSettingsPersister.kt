

package com.duckduckgo.autofill.impl.email.remoteconfig

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autofill.impl.email.incontext.EmailProtectionInContextSignupFeature
import com.duckduckgo.autofill.impl.email.incontext.store.EmailProtectionInContextDataStore
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
@RemoteFeatureStoreNamed(EmailProtectionInContextSignupFeature::class)
class EmailProtectionInContextRemoteSettingsPersister @Inject constructor(
    private val dataStore: EmailProtectionInContextDataStore,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : FeatureSettings.Store {

    override fun store(jsonString: String) {
        appCoroutineScope.launch(dispatchers.io()) {
            val json = JSONObject(jsonString)

            "installedDays".let {
                val installDays = if (json.has(it)) {
                    json.getInt(it)
                } else {
                    Int.MAX_VALUE
                }
                dataStore.updateMaximumPermittedDaysSinceInstallation(installDays)
            }
        }
    }
}
