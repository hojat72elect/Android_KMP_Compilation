

package com.duckduckgo.networkprotection.internal.feature

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.BuildFlavor
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.RemoteFeatureStoreNamed
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.networkprotection.internal.di.InternalNetPConfigTogglesDao
import com.duckduckgo.networkprotection.store.remote_config.NetPConfigToggle
import com.duckduckgo.networkprotection.store.remote_config.NetPConfigTogglesDao
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesBinding(AppScope::class)
@RemoteFeatureStoreNamed(NetPInternalFeatureToggles::class)
@SingleInstanceIn(AppScope::class)
class NetPInternalFeatureToggleStore @Inject constructor(
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val appBuildConfig: AppBuildConfig,
    @InternalNetPConfigTogglesDao private val togglesDao: NetPConfigTogglesDao,
    private val dispatcherProvider: DispatcherProvider,
) : Toggle.Store {

    private val togglesCache = ConcurrentHashMap<String, Toggle.State>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            togglesDao.getConfigToggles().forEach {
                togglesCache[it.name] = it.asToggleState()
            }
        }
    }

    override fun set(key: String, state: Toggle.State) {
        togglesCache[key] = state
        persistToggle(state.asNetPConfigToggle(key))
    }

    override fun get(key: String): Toggle.State? {
        return togglesCache[key]
    }

    private fun NetPConfigToggle.asToggleState(): Toggle.State {
        return Toggle.State(
            enable = this.enabled,
        )
    }

    private fun Toggle.State.asNetPConfigToggle(key: String): NetPConfigToggle {
        return NetPConfigToggle(
            name = key,
            enabled = enable,
        )
    }

    private fun persistToggle(toggle: NetPConfigToggle) {
        coroutineScope.launch(dispatcherProvider.io()) {
            // Remote configs will not override any value that has isManualOverride = true
            // But this is only for INTERNAL builds, because we have internal settings
            // In any other build that is not internal isManualOverride is always false
            togglesDao.insert(toggle.copy(isManualOverride = appBuildConfig.flavor == BuildFlavor.INTERNAL && toggle.isManualOverride))
        }
    }
}
