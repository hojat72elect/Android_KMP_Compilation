

package com.duckduckgo.privacy.config.store

interface PrivacyConfigRepository {
    fun insert(privacyConfig: PrivacyConfig)
    fun get(): PrivacyConfig?
    fun delete()
}

class RealPrivacyConfigRepository(database: PrivacyConfigDatabase) : PrivacyConfigRepository {

    private val privacyConfigDao: PrivacyConfigDao = database.privacyConfigDao()

    override fun insert(privacyConfig: PrivacyConfig) {
        privacyConfigDao.insert(privacyConfig)
    }

    override fun get(): PrivacyConfig? {
        return privacyConfigDao.get()
    }

    override fun delete() {
        privacyConfigDao.delete()
    }
}
