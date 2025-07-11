

package com.duckduckgo.app.trackerdetection

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.duckduckgo.app.global.uri.removeSubdomain
import com.duckduckgo.app.trackerdetection.db.TdsDomainEntityDao
import com.duckduckgo.app.trackerdetection.db.TdsEntityDao
import com.duckduckgo.app.trackerdetection.model.Entity
import com.duckduckgo.app.trackerdetection.model.TdsEntity
import com.duckduckgo.common.utils.baseHost
import com.duckduckgo.di.scopes.AppScope
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(AppScope::class)
class TdsEntityLookup @Inject constructor(
    private val entityDao: TdsEntityDao,
    private val domainEntityDao: TdsDomainEntityDao,
) : EntityLookup {

    var entities: List<TdsEntity> = emptyList()

    @WorkerThread
    override fun entityForUrl(url: String): Entity? {
        val uri = url.toUri()
        val host = uri.baseHost ?: return null

        // try searching for exact domain
        val direct = lookUpEntityInDatabase(host)
        if (direct != null) return direct

        // remove the first subdomain, and try again
        val parentDomain = uri.removeSubdomain() ?: return null
        return entityForUrl(parentDomain)
    }

    @WorkerThread
    override fun entityForUrl(uri: Uri): Entity? {
        val host = uri.host ?: return null

        // try searching for exact domain
        val direct = lookUpEntityInDatabase(host)
        if (direct != null) return direct

        // remove the first subdomain, and try again
        val parentDomain = uri.removeSubdomain() ?: return null
        return entityForUrl(parentDomain.toUri())
    }

    @WorkerThread
    override fun entityForName(name: String): Entity? {
        return entityDao.get(name)
    }

    @WorkerThread
    private fun lookUpEntityInDatabase(domain: String): Entity? {
        val domainEntity = domainEntityDao.get(domain) ?: return null
        return entityDao.get(domainEntity.entityName)
    }
}
