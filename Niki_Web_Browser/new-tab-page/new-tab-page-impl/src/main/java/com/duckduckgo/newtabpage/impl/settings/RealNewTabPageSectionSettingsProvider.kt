

package com.duckduckgo.newtabpage.impl.settings

import com.duckduckgo.common.utils.plugins.ActivePluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.newtabpage.api.NewTabPageSectionPlugin
import com.duckduckgo.newtabpage.api.NewTabPageSectionSettingsPlugin
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NewTabPageSectionSettingsProvider {
    fun provideSections(): Flow<List<NewTabPageSectionSettingsPlugin>>
}

@ContributesBinding(scope = AppScope::class)
class RealNewTabPageSectionSettingsProvider @Inject constructor(
    private val newTabSectionsSettingsPlugins: PluginPoint<NewTabPageSectionSettingsPlugin>,
    private val newTabSectionsPlugins: ActivePluginPoint<NewTabPageSectionPlugin>,
    private val newTabSettingsStore: NewTabSettingsStore,
) : NewTabPageSectionSettingsProvider {
    // we only show settings for sections that are enabled via remote config
    override fun provideSections(): Flow<List<NewTabPageSectionSettingsPlugin>> = flow {
        val plugins = mutableListOf<NewTabPageSectionSettingsPlugin>()
        val sectionSettingsPlugins = newTabSectionsSettingsPlugins.getPlugins().filter { it.isActive() }
        val sections = newTabSectionsPlugins.getPlugins()
        val userSections = newTabSettingsStore.sectionSettings

        userSections.forEach { section ->
            val sectionPlugin = sections.find { it.name == section }
            if (sectionPlugin != null) {
                // if there is a view that implements the settings plugin, we show it
                val sectionSettingsPlugin = sectionSettingsPlugins.find { it.name == section }
                if (sectionSettingsPlugin != null) {
                    plugins.add(sectionSettingsPlugin)
                }
            }
        }
        emit(plugins)
    }
}
