

package com.duckduckgo.app.privacy.db

import android.net.Uri
import androidx.core.net.toUri
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.domain
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealUserAllowListRepository @Inject constructor(
    private val userAllowListDao: UserAllowListDao,
    @AppCoroutineScope appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    @IsMainProcess isMainProcess: Boolean,
) : UserAllowListRepository {

    private val userAllowList = CopyOnWriteArrayList<String>()
    override fun isUrlInUserAllowList(url: String): Boolean {
        return isUriInUserAllowList(url.toUri())
    }

    override fun isUriInUserAllowList(uri: Uri): Boolean {
        return isDomainInUserAllowList(uri.domain())
    }

    override fun isDomainInUserAllowList(domain: String?): Boolean {
        return userAllowList.contains(domain)
    }

    override fun domainsInUserAllowList(): List<String> {
        return userAllowList
    }

    override fun domainsInUserAllowListFlow(): Flow<List<String>> {
        return all()
            .onStart { emit(userAllowList.toList()) }
            .distinctUntilChanged()
    }

    override suspend fun addDomainToUserAllowList(domain: String) {
        withContext(dispatcherProvider.io()) {
            userAllowListDao.insert(domain)
        }
    }

    override suspend fun removeDomainFromUserAllowList(domain: String) {
        withContext(dispatcherProvider.io()) {
            userAllowListDao.delete(domain)
        }
    }

    init {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                all().collect { list ->
                    userAllowList.clear()
                    userAllowList.addAll(list)
                }
            }
        }
    }

    private fun all(): Flow<List<String>> {
        return userAllowListDao.allDomainsFlow()
    }
}
