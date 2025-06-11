

package com.duckduckgo.privacyprotectionspopup.impl.db

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PopupDismissDomainRepository {
    fun getPopupDismissTime(domain: String): Flow<Instant?>
    suspend fun setPopupDismissTime(domain: String, time: Instant)
    suspend fun removeEntriesOlderThan(time: Instant)
    suspend fun removeAllEntries()
}

@ContributesBinding(AppScope::class)
class PopupDismissDomainRepositoryImpl @Inject constructor(
    private val dao: PopupDismissDomainsDao,
) : PopupDismissDomainRepository {

    override fun getPopupDismissTime(domain: String): Flow<Instant?> =
        dao.query(domain).map { it?.dismissedAt }

    override suspend fun setPopupDismissTime(
        domain: String,
        time: Instant,
    ) {
        dao.insert(PopupDismissDomain(domain = domain, dismissedAt = time))
    }

    override suspend fun removeEntriesOlderThan(time: Instant) {
        dao.removeEntriesOlderThan(time)
    }

    override suspend fun removeAllEntries() {
        dao.removeAllEntries()
    }
}
