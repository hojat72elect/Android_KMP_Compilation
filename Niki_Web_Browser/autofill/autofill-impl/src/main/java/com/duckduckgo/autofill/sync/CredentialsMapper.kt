

package com.duckduckgo.autofill.sync

import com.duckduckgo.autofill.api.domain.app.LoginCredentials
import com.duckduckgo.common.utils.formatters.time.DatabaseDateFormatter
import com.duckduckgo.sync.api.SyncCrypto
import javax.inject.Inject

class CredentialsSyncMapper @Inject constructor(
    private val syncCrypto: SyncCrypto,
) {
    fun toLoginCredential(
        remoteEntry: CredentialsSyncEntryResponse,
        localId: Long? = null,
        lastModified: String,
    ): LoginCredentials {
        return LoginCredentials(
            id = localId,
            domain = remoteEntry.domain?.decrypt(),
            username = remoteEntry.username?.decrypt(),
            password = remoteEntry.password?.decrypt(),
            domainTitle = remoteEntry.title.decrypt(),
            notes = remoteEntry.notes.decrypt(),
            lastUpdatedMillis = DatabaseDateFormatter.parseIso8601ToMillis(remoteEntry.last_modified ?: lastModified),
        )
    }

    private fun String?.decrypt(): String {
        return if (this == null) "" else syncCrypto.decrypt(this)
    }
}
