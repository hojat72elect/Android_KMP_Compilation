

package com.duckduckgo.app.surrogates

import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.surrogates.store.ResourceSurrogateDataStore
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import java.io.ByteArrayInputStream
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@WorkerThread
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class ResourceSurrogateLoader @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val resourceSurrogates: ResourceSurrogates,
    private val surrogatesDataStore: ResourceSurrogateDataStore,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        appCoroutineScope.launch(dispatcherProvider.io()) { loadData() }
    }

    fun loadData() {
        Timber.v("Loading surrogate data")
        if (surrogatesDataStore.hasData()) {
            val bytes = surrogatesDataStore.loadData()
            resourceSurrogates.loadSurrogates(convertBytes(bytes))
        }
    }

    @VisibleForTesting
    fun convertBytes(bytes: ByteArray): List<SurrogateResponse> {
        return try {
            parse(bytes)
        } catch (e: Throwable) {
            Timber.w(e, "Failed to parse surrogates file; file may be corrupt or badly formatted")
            emptyList()
        }
    }

    private fun parse(bytes: ByteArray): List<SurrogateResponse> {
        val surrogates = mutableListOf<SurrogateResponse>()
        val existingLines = readExistingLines(bytes)

        var nextLineIsNewRule = true

        var scriptId = ""
        var ruleName = ""
        var mimeType = ""
        val functionBuilder = StringBuilder()

        existingLines.forEach {
            if (it.startsWith("#")) {
                return@forEach
            }

            if (nextLineIsNewRule) {
                with(it.split(" ")) {
                    ruleName = this[0]
                    mimeType = this[1]
                }
                with(ruleName.split("/")) {
                    scriptId = this.last()
                }
                Timber.d("Found new surrogate rule: $scriptId - $ruleName - $mimeType")
                nextLineIsNewRule = false
                return@forEach
            }

            if (it.isBlank()) {
                surrogates.add(
                    SurrogateResponse(
                        scriptId = scriptId,
                        name = ruleName,
                        mimeType = mimeType,
                        jsFunction = functionBuilder.toString(),
                    ),
                )

                functionBuilder.setLength(0)

                nextLineIsNewRule = true
                return@forEach
            }

            functionBuilder.append(it)
            functionBuilder.append("\n")
        }

        Timber.d("Processed ${surrogates.size} surrogates")
        return surrogates
    }

    private fun readExistingLines(bytes: ByteArray): List<String> {
        val existingLines = ByteArrayInputStream(bytes).bufferedReader().use { reader ->
            reader.readLines().toMutableList()
        }

        if (existingLines.isNotEmpty() && existingLines.last().isNotBlank()) {
            existingLines.add("")
        }
        return existingLines
    }
}
