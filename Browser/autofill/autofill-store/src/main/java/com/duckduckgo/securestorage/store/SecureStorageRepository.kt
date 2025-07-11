

package com.duckduckgo.securestorage.store

import com.duckduckgo.securestorage.store.db.NeverSavedSiteEntity
import com.duckduckgo.securestorage.store.db.NeverSavedSitesDao
import com.duckduckgo.securestorage.store.db.WebsiteLoginCredentialsDao
import com.duckduckgo.securestorage.store.db.WebsiteLoginCredentialsEntity
import kotlinx.coroutines.flow.Flow

/**
 * This class is mainly responsible only for accessing and storing data into the DB.
 */
interface SecureStorageRepository {
    interface Factory {
        suspend fun get(): SecureStorageRepository?
    }

    suspend fun addWebsiteLoginCredential(websiteLoginCredentials: WebsiteLoginCredentialsEntity): WebsiteLoginCredentialsEntity?

    suspend fun addWebsiteLoginCredentials(list: List<WebsiteLoginCredentialsEntity>): List<Long>

    suspend fun websiteLoginCredentialsForDomain(domain: String): Flow<List<WebsiteLoginCredentialsEntity>>

    suspend fun getWebsiteLoginCredentialsForId(id: Long): WebsiteLoginCredentialsEntity?

    suspend fun websiteLoginCredentials(): Flow<List<WebsiteLoginCredentialsEntity>>

    suspend fun updateWebsiteLoginCredentials(websiteLoginCredentials: WebsiteLoginCredentialsEntity): WebsiteLoginCredentialsEntity?

    suspend fun deleteWebsiteLoginCredentials(id: Long)

    suspend fun deleteWebsiteLoginCredentials(ids: List<Long>)

    suspend fun addToNeverSaveList(domain: String)

    suspend fun clearNeverSaveList()

    suspend fun neverSaveListCount(): Flow<Int>

    suspend fun isInNeverSaveList(domain: String): Boolean
}

class RealSecureStorageRepository(
    private val websiteLoginCredentialsDao: WebsiteLoginCredentialsDao,
    private val neverSavedSitesDao: NeverSavedSitesDao,
) : SecureStorageRepository {

    override suspend fun addWebsiteLoginCredential(websiteLoginCredentials: WebsiteLoginCredentialsEntity): WebsiteLoginCredentialsEntity? {
        val newCredentialId = websiteLoginCredentialsDao.insert(websiteLoginCredentials)
        return websiteLoginCredentialsDao.getWebsiteLoginCredentialsById(newCredentialId)
    }

    override suspend fun addWebsiteLoginCredentials(list: List<WebsiteLoginCredentialsEntity>): List<Long> {
        return websiteLoginCredentialsDao.insert(list)
    }

    override suspend fun websiteLoginCredentialsForDomain(domain: String): Flow<List<WebsiteLoginCredentialsEntity>> {
        return if (domain.isEmpty()) {
            websiteLoginCredentialsDao.websiteLoginCredentialsWithoutDomain()
        } else {
            websiteLoginCredentialsDao.websiteLoginCredentialsByDomain(domain)
        }
    }

    override suspend fun websiteLoginCredentials(): Flow<List<WebsiteLoginCredentialsEntity>> =
        websiteLoginCredentialsDao.websiteLoginCredentials()

    override suspend fun getWebsiteLoginCredentialsForId(id: Long): WebsiteLoginCredentialsEntity? =
        websiteLoginCredentialsDao.getWebsiteLoginCredentialsById(id)

    override suspend fun updateWebsiteLoginCredentials(websiteLoginCredentials: WebsiteLoginCredentialsEntity): WebsiteLoginCredentialsEntity? {
        val credentialId = websiteLoginCredentials.id
        websiteLoginCredentialsDao.update(websiteLoginCredentials)
        return websiteLoginCredentialsDao.getWebsiteLoginCredentialsById(credentialId)
    }

    override suspend fun deleteWebsiteLoginCredentials(id: Long) {
        websiteLoginCredentialsDao.delete(id)
    }

    override suspend fun deleteWebsiteLoginCredentials(ids: List<Long>) {
        websiteLoginCredentialsDao.delete(ids)
    }

    override suspend fun addToNeverSaveList(domain: String) {
        neverSavedSitesDao.insert(NeverSavedSiteEntity(domain = domain))
    }

    override suspend fun clearNeverSaveList() {
        neverSavedSitesDao.clear()
    }

    override suspend fun neverSaveListCount(): Flow<Int> {
        return neverSavedSitesDao.count()
    }

    override suspend fun isInNeverSaveList(domain: String): Boolean {
        return neverSavedSitesDao.isInNeverSaveList(domain)
    }
}
