

package com.duckduckgo.privacy.config.impl

import com.duckduckgo.experiments.api.VariantConfig
import com.duckduckgo.experiments.api.VariantManager
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import timber.log.Timber

internal class VariantManagerPlugin constructor(
    private val variantManager: VariantManager,
) : PrivacyFeaturePlugin {

    override fun store(
        featureName: String,
        jsonString: String,
    ): Boolean {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<VariantManagerConfig> =
            moshi.adapter(VariantManagerConfig::class.java)

        val variantManagerConfig: VariantManagerConfig? = runCatching {
            jsonAdapter.fromJson(jsonString)
        }.onFailure {
            Timber.e(
                """
                    Error: ${it.message}
                    Parsing jsonString: $jsonString
                """.trimIndent(),
            )
        }.getOrThrow()

        return variantManagerConfig?.variants?.takeIf { it.isNotEmpty() }?.let { variants ->
            variantManager.updateVariants(variants)
            true
        } ?: false
    }

    override val featureName = VARIANT_MANAGER_FEATURE_NAME

    companion object {
        const val VARIANT_MANAGER_FEATURE_NAME = "experimentalVariants"
    }
}

internal data class VariantManagerConfig(
    val variants: List<VariantConfig>,
)
