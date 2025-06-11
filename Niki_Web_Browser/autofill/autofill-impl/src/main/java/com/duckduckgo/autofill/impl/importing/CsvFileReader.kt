

package com.duckduckgo.autofill.impl.importing

import android.content.Context
import android.net.Uri
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface CsvFileReader {
    suspend fun readCsvFile(fileUri: Uri): String
}

@ContributesBinding(AppScope::class)
class ContentResolverFileReader @Inject constructor(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : CsvFileReader {

    override suspend fun readCsvFile(fileUri: Uri): String {
        return withContext(dispatchers.io()) {
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    buildString {
                        reader.forEachLine { line ->
                            append(line).append("\n")
                        }
                    }
                }
            } ?: ""
        }
    }
}
