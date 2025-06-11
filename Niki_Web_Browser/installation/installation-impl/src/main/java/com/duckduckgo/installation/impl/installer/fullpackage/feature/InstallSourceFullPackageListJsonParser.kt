

package com.duckduckgo.installation.impl.installer.fullpackage.feature

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.installation.impl.installer.fullpackage.InstallSourceFullPackageStore.IncludedPackages
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

interface InstallSourceFullPackageListJsonParser {
    suspend fun parseJson(json: String?): IncludedPackages
}

@ContributesBinding(AppScope::class)
class InstallSourceFullPackageListJsonParserImpl @Inject constructor() : InstallSourceFullPackageListJsonParser {

    private val jsonAdapter by lazy { buildJsonAdapter() }

    private fun buildJsonAdapter(): JsonAdapter<SettingsJson> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(SettingsJson::class.java)
    }

    override suspend fun parseJson(json: String?): IncludedPackages {
        if (json == null) return IncludedPackages()

        return kotlin.runCatching {
            val parsed = jsonAdapter.fromJson(json)
            return parsed?.asIncludedPackages() ?: IncludedPackages()
        }.getOrDefault(IncludedPackages())
    }

    private fun SettingsJson.asIncludedPackages(): IncludedPackages {
        return IncludedPackages(includedPackages.map { it })
    }

    private data class SettingsJson(
        val includedPackages: List<String>,
    )
}
