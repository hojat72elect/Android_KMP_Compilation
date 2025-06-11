

package com.duckduckgo.installation.impl.installer.fullpackage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.installation.impl.installer.di.InstallerModule.InstallSourceFullPackageDataStore
import com.duckduckgo.installation.impl.installer.fullpackage.InstallSourceFullPackageStore.IncludedPackages
import com.duckduckgo.installation.impl.installer.fullpackage.feature.InstallSourceFullPackageListJsonParser
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface InstallSourceFullPackageStore {
    suspend fun updateInstallSourceFullPackages(json: String)
    suspend fun getInstallSourceFullPackages(): IncludedPackages

    data class IncludedPackages(val list: List<String> = emptyList()) {

        fun hasWildcard(): Boolean {
            return list.contains("*")
        }
    }
}

@ContributesBinding(AppScope::class, boundType = InstallSourceFullPackageStore::class)
@SingleInstanceIn(AppScope::class)
class InstallSourceFullPackageStoreImpl @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val jsonParser: InstallSourceFullPackageListJsonParser,
    @InstallSourceFullPackageDataStore private val dataStore: DataStore<Preferences>,
) : InstallSourceFullPackageStore {

    override suspend fun updateInstallSourceFullPackages(json: String) {
        withContext(dispatchers.io()) {
            val includedPackages = jsonParser.parseJson(json)
            dataStore.edit {
                it[packageInstallersKey] = includedPackages.list.toSet()
            }
        }
    }

    override suspend fun getInstallSourceFullPackages(): IncludedPackages {
        return withContext(dispatchers.io()) {
            val packageInstallers = dataStore.data.map { it[packageInstallersKey] }.firstOrNull()
            return@withContext IncludedPackages(packageInstallers?.toList() ?: emptyList())
        }
    }

    companion object {
        val packageInstallersKey = stringSetPreferencesKey("package_installers")
    }
}
