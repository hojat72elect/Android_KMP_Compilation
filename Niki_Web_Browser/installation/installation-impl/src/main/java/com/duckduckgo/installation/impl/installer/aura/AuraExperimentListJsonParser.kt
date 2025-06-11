

package com.duckduckgo.installation.impl.installer.aura

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject
import kotlinx.coroutines.withContext

data class Packages(val list: List<String> = emptyList())

interface AuraExperimentListJsonParser {
    suspend fun parseJson(json: String?): Packages
}

@ContributesBinding(AppScope::class)
class AuraExperimentListJsonParserImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) : AuraExperimentListJsonParser {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    private fun buildJsonAdapter(): JsonAdapter<SettingsJson> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(SettingsJson::class.java)
    }

    override suspend fun parseJson(json: String?): Packages = withContext(dispatcherProvider.io()) {
        if (json == null) return@withContext Packages()

        kotlin.runCatching {
            val parsed = jsonAdapter.fromJson(json)
            parsed?.asPackages() ?: Packages()
        }.getOrDefault(Packages())
    }

    private fun SettingsJson.asPackages(): Packages {
        return Packages(packages.map { it })
    }

    private data class SettingsJson(
        val packages: List<String>,
    )
}
