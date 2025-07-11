

package com.duckduckgo.autofill.impl.sharedcreds

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.Moshi
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

interface SharedCredentialJsonReader {
    suspend fun read(): String?
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AppleSharedCredentialsJsonReader @Inject constructor(
    private val moshi: Moshi,
    private val dispatchers: DispatcherProvider,
) : SharedCredentialJsonReader {

    override suspend fun read(): String? {
        return withContext(dispatchers.io()) {
            loadJson()
        }
    }

    private fun loadJson(): String? {
        val json = runCatching {
            val reader = javaClass.classLoader?.getResource(JSON_FILENAME)?.openStream()?.bufferedReader()
            reader.use { it?.readText() }
        }.getOrNull()

        if (json == null) {
            Timber.e("Failed to load shared credentials json")
        }

        return json
    }

    private companion object {
        private const val JSON_FILENAME = "shared-credentials.json"
    }
}
