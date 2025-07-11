

package com.duckduckgo.app.tabs.ui

import com.duckduckgo.app.tabs.model.TabEntity

sealed class TabSwitcherItem(val id: String) {
    sealed class Tab(val tabEntity: TabEntity) : TabSwitcherItem(tabEntity.tabId) {
        data class NormalTab(
            private val entity: TabEntity,
            val isActive: Boolean,
        ) : Tab(entity)

        data class SelectableTab(
            private val entity: TabEntity,
            val isSelected: Boolean,
        ) : Tab(entity)

        val isNewTabPage: Boolean
            get() = tabEntity.url.isNullOrBlank()
    }

    data class TrackerAnimationInfoPanel(val trackerCount: Int) : TabSwitcherItem(TRACKER_ANIMATION_PANEL_ID) {
        companion object {
            const val ANIMATED_TILE_NO_REPLACE_ALPHA = 0.4f
            const val ANIMATED_TILE_DEFAULT_ALPHA = 1f
            const val TRACKER_ANIMATION_PANEL_ID = "TrackerAnimationInfoPanel"
        }
    }
}
