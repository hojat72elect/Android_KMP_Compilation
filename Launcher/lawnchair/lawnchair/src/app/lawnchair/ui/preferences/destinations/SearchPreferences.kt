package app.lawnchair.ui.preferences.destinations

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lawnchair.ui.preferences.LocalIsExpandedScreen
import app.lawnchair.ui.preferences.LocalNavController
import app.lawnchair.ui.preferences.components.controls.ClickablePreference
import app.lawnchair.ui.preferences.components.layout.PreferenceGroup
import app.lawnchair.ui.preferences.components.layout.TwoTabPreferenceLayout
import app.lawnchair.ui.preferences.components.search.DockSearchPreference
import app.lawnchair.ui.preferences.components.search.DrawerSearchPreference
import app.lawnchair.ui.preferences.navigation.Routes
import com.android.launcher3.R

enum class SearchRoute {
    DOCK_SEARCH,
    DRAWER_SEARCH,
}

@Composable
fun SearchBarPreference(
    id: SearchRoute,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
) {
    val navController = LocalNavController.current
    val preference = remember {
        movableContentOf {
            ClickablePreference(
                label = stringResource(R.string.search_bar_settings),
                modifier = modifier,
            ) {
                navController.navigate(route = "${Routes.SEARCH}/${id.ordinal}")
            }
        }
    }

    if (showLabel) {
        PreferenceGroup(
            heading = stringResource(id = R.string.search_bar_label),
        ) {
            preference()
        }
    } else {
        preference()
    }
}

@Composable
fun SearchPreferences(
    modifier: Modifier = Modifier,
    currentTab: Int = 0,
) {
    TwoTabPreferenceLayout(
        label = stringResource(id = R.string.search_bar_label),
        backArrowVisible = !LocalIsExpandedScreen.current,
        defaultPage = currentTab,
        firstPageLabel = stringResource(id = R.string.dock_label),
        firstPageContent = {
            DockSearchPreference()
        },
        secondPageLabel = stringResource(id = R.string.app_drawer_label),
        secondPageContent = {
            Spacer(Modifier.height(8.dp))
            DrawerSearchPreference()
        },
        modifier = modifier,
    )
}
