

package com.duckduckgo.experiments.impl

import com.duckduckgo.app.statistics.store.StatisticsDataStore
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.experiments.impl.VariantManagerImpl.Companion.DEFAULT_VARIANT
import com.duckduckgo.experiments.impl.reinstalls.REINSTALL_VARIANT
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

interface ExperimentVariantRepository {
    fun getUserVariant(): String?
    fun updateVariant(variantKey: String)
    fun getAppReferrerVariant(): String?
    fun updateAppReferrerVariant(variant: String)
}

@ContributesBinding(AppScope::class)
class ExperimentVariantRepositoryImpl @Inject constructor(
    private val store: StatisticsDataStore,
) : ExperimentVariantRepository {

    override fun getUserVariant(): String? = store.variant

    override fun updateVariant(variantKey: String) {
        Timber.i("Updating variant for user: $variantKey")
        if (updateVariantIsAllowed()) {
            store.variant = variantKey
        }
    }

    private fun updateVariantIsAllowed(): Boolean {
        return store.variant != DEFAULT_VARIANT.key && store.variant != REINSTALL_VARIANT
    }

    override fun getAppReferrerVariant(): String? = store.referrerVariant

    override fun updateAppReferrerVariant(variant: String) {
        Timber.i("Updating variant for app referer: $variant")
        store.variant = variant
        store.referrerVariant = variant
    }
}
