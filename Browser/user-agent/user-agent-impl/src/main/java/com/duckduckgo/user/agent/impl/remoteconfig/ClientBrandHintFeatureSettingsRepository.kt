

package com.duckduckgo.user.agent.impl.remoteconfig

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.user.agent.impl.store.ClientBrandHintDatabase
import com.duckduckgo.user.agent.impl.store.ClientHintBrandDomainEntity
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

interface ClientBrandHintFeatureSettingsRepository {
    fun updateAllSettings(settings: ClientBrandHintSettings)
    val clientBrandHints: CopyOnWriteArrayList<ClientBrandHintDomain>
}

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealClientBrandHintFeatureSettingsRepository @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider,
    database: ClientBrandHintDatabase,
    @IsMainProcess isMainProcess: Boolean,
) : ClientBrandHintFeatureSettingsRepository {

    private val dao = database.clientBrandHintDao()

    override val clientBrandHints = CopyOnWriteArrayList<ClientBrandHintDomain>()

    init {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                loadToMemory()
            }
        }
    }

    override fun updateAllSettings(settings: ClientBrandHintSettings) {
        Timber.i("ClientBrandHintProvider: update domains to ${settings.domains}")
        dao.updateAllDomains(settings.domains.map { ClientHintBrandDomainEntity(it.domain, it.brand.name) })
        loadToMemory()
    }

    private fun loadToMemory() {
        clientBrandHints.clear()
        val clientBrandHintsDomainList = dao.getAllDomains()
        Timber.i("ClientBrandHintProvider: loading domains to memory $clientBrandHintsDomainList")
        clientBrandHints.addAll(clientBrandHintsDomainList.map { ClientBrandHintDomain(it.url, ClientBrandsHints.from(it.brand)) })
    }
}
