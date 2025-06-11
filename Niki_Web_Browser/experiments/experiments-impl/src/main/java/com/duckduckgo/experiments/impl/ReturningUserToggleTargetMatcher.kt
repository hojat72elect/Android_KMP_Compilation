

package com.duckduckgo.experiments.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.experiments.api.VariantManager
import com.duckduckgo.experiments.impl.reinstalls.REINSTALL_VARIANT
import com.duckduckgo.feature.toggles.api.Toggle.State.Target
import com.duckduckgo.feature.toggles.api.Toggle.TargetMatcherPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class ReturningUserToggleTargetMatcher @Inject constructor(
    private val variantManager: VariantManager,
) : TargetMatcherPlugin {
    override fun matchesTargetProperty(target: Target): Boolean {
        return target.isReturningUser?.let { isReturningUserTarget ->
            val isReturningUser = variantManager.getVariantKey() == REINSTALL_VARIANT
            isReturningUserTarget == isReturningUser
        } ?: true
    }
}
