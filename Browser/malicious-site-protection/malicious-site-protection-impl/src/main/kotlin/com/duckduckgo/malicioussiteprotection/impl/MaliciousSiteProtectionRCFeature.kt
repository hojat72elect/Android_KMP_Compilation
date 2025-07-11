

package com.duckduckgo.malicioussiteprotection.impl

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

interface MaliciousSiteProtectionRCFeature {
    fun isFeatureEnabled(): Boolean
    fun canUpdateDatasets(): Boolean
    fun scamProtectionEnabled(): Boolean
    fun getHashPrefixUpdateFrequency(): Long
    fun getFilterSetUpdateFrequency(): Long
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class, MaliciousSiteProtectionRCFeature::class)
@ContributesMultibinding(AppScope::class, PrivacyConfigCallbackPlugin::class)
class RealMaliciousSiteProtectionRCFeature @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val maliciousSiteProtectionFeature: MaliciousSiteProtectionFeature,
    private val appBuildConfig: AppBuildConfig,
    @IsMainProcess private val isMainProcess: Boolean,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) : MaliciousSiteProtectionRCFeature, PrivacyConfigCallbackPlugin {
    private var isFeatureEnabled = false
    private var canUpdateDatasets = false
    private var scamProtection = false

    private var hashPrefixUpdateFrequency = 20L
    private var filterSetUpdateFrequency = 720L

    init {
        if (isMainProcess) {
            loadToMemory()
        }
    }

    override fun onPrivacyConfigDownloaded() {
        loadToMemory()
    }

    override fun getFilterSetUpdateFrequency(): Long {
        return filterSetUpdateFrequency
    }

    override fun getHashPrefixUpdateFrequency(): Long {
        return hashPrefixUpdateFrequency
    }

    override fun isFeatureEnabled(): Boolean {
        return isFeatureEnabled
    }

    override fun canUpdateDatasets(): Boolean {
        return canUpdateDatasets
    }

    override fun scamProtectionEnabled(): Boolean {
        return scamProtection
    }

    private fun loadToMemory() {
        appCoroutineScope.launch(dispatchers.io()) {
            // MSP is disabled in F-Droid builds, as we can't download datasets
            isFeatureEnabled = maliciousSiteProtectionFeature.self().isEnabled() &&
                    maliciousSiteProtectionFeature.visibleAndOnByDefault().isEnabled()
            scamProtection = isFeatureEnabled && maliciousSiteProtectionFeature.scamProtection().isEnabled()
            canUpdateDatasets = maliciousSiteProtectionFeature.canUpdateDatasets().isEnabled()
            maliciousSiteProtectionFeature.self().getSettings()?.let {
                JSONObject(it).let { settings ->
                    hashPrefixUpdateFrequency = settings.getLong("hashPrefixUpdateFrequency")
                    filterSetUpdateFrequency = settings.getLong("filterSetUpdateFrequency")
                }
            }
        }
    }
}
