

package com.duckduckgo.common.ui.internal.experiments.visual

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.ui.internal.experiments.ExperimentalUIPlugin
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = ExperimentalUIPlugin::class,
)
@Suppress("unused")
interface ExperimentalUIPluginPoint
