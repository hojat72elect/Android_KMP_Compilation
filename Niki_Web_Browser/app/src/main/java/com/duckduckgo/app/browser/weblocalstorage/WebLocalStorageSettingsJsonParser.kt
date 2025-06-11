

package com.duckduckgo.app.browser.weblocalstorage

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject
import kotlinx.coroutines.withContext

data class Domains(val list: List<String> = emptyList())
data class MatchingRegex(val list: List<String> = emptyList())

data class WebLocalStorageSettings(
    val domains: Domains = Domains(),
    val matchingRegex: MatchingRegex = MatchingRegex(),
)

interface WebLocalStorageSettingsJsonParser {
    suspend fun parseJson(json: String?): WebLocalStorageSettings
}

@ContributesBinding(AppScope::class)
class WebLocalStorageSettingsJsonParserImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) : WebLocalStorageSettingsJsonParser {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    private fun buildJsonAdapter(): JsonAdapter<SettingsJson> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(SettingsJson::class.java)
    }

    override suspend fun parseJson(json: String?): WebLocalStorageSettings = withContext(dispatcherProvider.io()) {
        if (json == null) return@withContext WebLocalStorageSettings(Domains(), MatchingRegex())

        kotlin.runCatching {
            val parsed = jsonAdapter.fromJson(json)
            val domains = parsed?.asDomains() ?: Domains()
            val matchingRegex = parsed?.asMatchingRegex() ?: MatchingRegex()
            WebLocalStorageSettings(domains, matchingRegex)
        }.getOrDefault(WebLocalStorageSettings(Domains(), MatchingRegex()))
    }

    private fun SettingsJson.asDomains(): Domains {
        return Domains(domains.map { it })
    }

    private fun SettingsJson.asMatchingRegex(): MatchingRegex {
        return MatchingRegex(matchingRegex.map { it })
    }

    private data class SettingsJson(
        val domains: List<String>,
        val matchingRegex: List<String>,
    )
}
