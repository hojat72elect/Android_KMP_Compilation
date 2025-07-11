

package com.duckduckgo.feature.toggles.impl

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureTogglesInventory
import com.duckduckgo.feature.toggles.api.Toggle
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
class RealFeatureTogglesInventory @Inject constructor(
    private val toggles: DaggerSet<FeatureTogglesInventory>,
    private val dispatcherProvider: DispatcherProvider,
) : FeatureTogglesInventory {
    override suspend fun getAll(): List<Toggle> = withContext(dispatcherProvider.io()) {
        return@withContext toggles.flatMap { it.getAll() }.distinctBy { it.featureName() }
    }

    override suspend fun getAllTogglesForParent(name: String): List<Toggle> = withContext(dispatcherProvider.io()) {
        return@withContext getAll().filter { it.featureName().parentName == name }
    }

    override suspend fun getAllActiveExperimentToggles(): List<Toggle> = withContext(dispatcherProvider.io()) {
        return@withContext getAll().filter { it.getCohort() != null && it.isEnabled() }
    }
}
