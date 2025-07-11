

package com.duckduckgo.app.fakes

import android.net.Uri
import com.duckduckgo.app.privacy.db.UserAllowListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserAllowListRepositoryFake : UserAllowListRepository {
    override fun isUrlInUserAllowList(url: String): Boolean = false

    override fun isUriInUserAllowList(uri: Uri): Boolean = false

    override fun isDomainInUserAllowList(domain: String?): Boolean = false

    override fun domainsInUserAllowList(): List<String> = emptyList()

    override fun domainsInUserAllowListFlow(): Flow<List<String>> = flowOf(emptyList())

    override suspend fun addDomainToUserAllowList(domain: String) = Unit

    override suspend fun removeDomainFromUserAllowList(domain: String) = Unit
}
