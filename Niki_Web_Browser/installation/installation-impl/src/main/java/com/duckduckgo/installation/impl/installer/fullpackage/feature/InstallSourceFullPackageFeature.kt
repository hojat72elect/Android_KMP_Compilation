

package com.duckduckgo.installation.impl.installer.fullpackage.feature

import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureSettings
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.installation.impl.installer.fullpackage.InstallSourceFullPackageStore
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesRemoteFeature(
    scope = AppScope::class,
    boundType = InstallSourceFullPackageFeature::class,
    featureName = "sendFullPackageInstallSource",
    settingsStore = InstallSourceFullPackageFeatureSettingsStore::class,
)
/**
 * This is the class that represents the feature flag for sending full installer package ID.
 * This can be used to specify which app-installer package IDs we'd match on to send a pixel.
 * A wildcard "*" can be used to match all package IDs.
 */
interface InstallSourceFullPackageFeature {
    /**
     * @return `true` when the remote config has the global "sendFullPackageInstallSource" feature flag enabled
     *
     * If the remote feature is not present defaults to `false`
     */

    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(InstallSourceFullPackageFeature::class)
class InstallSourceFullPackageFeatureSettingsStore @Inject constructor(
    private val dataStore: InstallSourceFullPackageStore,
    private val dispatchers: DispatcherProvider,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) : FeatureSettings.Store {

    override fun store(jsonString: String) {
        kotlin.runCatching {
            appCoroutineScope.launch(dispatchers.io()) {
                dataStore.updateInstallSourceFullPackages(jsonString)
            }
        }
    }
}
