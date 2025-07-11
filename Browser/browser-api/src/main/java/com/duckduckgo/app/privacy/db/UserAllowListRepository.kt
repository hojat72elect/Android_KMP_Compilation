

package com.duckduckgo.app.privacy.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface UserAllowListRepository {
    fun isUrlInUserAllowList(url: String): Boolean
    fun isUriInUserAllowList(uri: Uri): Boolean
    fun isDomainInUserAllowList(domain: String?): Boolean
    fun domainsInUserAllowList(): List<String>
    fun domainsInUserAllowListFlow(): Flow<List<String>>
    suspend fun addDomainToUserAllowList(domain: String)
    suspend fun removeDomainFromUserAllowList(domain: String)
}
