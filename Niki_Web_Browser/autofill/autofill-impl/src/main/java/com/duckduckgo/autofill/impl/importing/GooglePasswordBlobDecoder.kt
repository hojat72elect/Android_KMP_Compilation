

package com.duckduckgo.autofill.impl.importing

import android.util.Base64
import android.util.Base64.DEFAULT
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface GooglePasswordBlobDecoder {
    suspend fun decode(data: String): String
}

@ContributesBinding(AppScope::class)
class GooglePasswordBlobDecoderImpl @Inject constructor(
    private val dispatchers: DispatcherProvider,
) : GooglePasswordBlobDecoder {

    override suspend fun decode(data: String): String {
        return withContext(dispatchers.io()) {
            kotlin.runCatching {
                val base64Data = removeDataTypePrefix(data)
                val decodedBytes = Base64.decode(base64Data, DEFAULT)
                String(decodedBytes, Charsets.UTF_8)
            }.getOrElse { rootCause ->
                throw IllegalArgumentException("Unrecognized format", rootCause)
            }
        }
    }

    /**
     * String will start with data type.
     * e.g., data:text/csv;charset=utf-8;;base64,
     */
    private fun removeDataTypePrefix(data: String) = data.split(",")[1]
}
