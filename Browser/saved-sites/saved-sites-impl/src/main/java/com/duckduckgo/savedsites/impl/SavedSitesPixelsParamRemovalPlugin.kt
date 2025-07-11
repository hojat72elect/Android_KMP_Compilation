

package com.duckduckgo.savedsites.impl

import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin
import com.duckduckgo.common.utils.plugins.pixel.PixelParamRemovalPlugin.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.BOOKMARK_LAUNCHED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.BOOKMARK_MENU_ADD_FAVORITE_CLICKED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.BOOKMARK_MENU_DELETE_BOOKMARK_CLICKED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.BOOKMARK_MENU_EDIT_BOOKMARK_CLICKED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.BOOKMARK_MENU_REMOVE_FAVORITE_CLICKED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.EDIT_BOOKMARK_ADD_FAVORITE_TOGGLED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.EDIT_BOOKMARK_DELETE_BOOKMARK_CANCELLED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.EDIT_BOOKMARK_DELETE_BOOKMARK_CLICKED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.EDIT_BOOKMARK_DELETE_BOOKMARK_CONFIRMED
import com.duckduckgo.savedsites.impl.SavedSitesPixelName.EDIT_BOOKMARK_REMOVE_FAVORITE_TOGGLED
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SavedSitesPixelsParamRemovalPlugin @Inject constructor() : PixelParamRemovalPlugin {

    override fun names(): List<Pair<String, Set<PixelParameter>>> {
        return listOf(
            BOOKMARK_LAUNCHED.pixelName to PixelParameter.removeAtb(),
            EDIT_BOOKMARK_ADD_FAVORITE_TOGGLED.pixelName to PixelParameter.removeAtb(),
            EDIT_BOOKMARK_REMOVE_FAVORITE_TOGGLED.pixelName to PixelParameter.removeAtb(),
            EDIT_BOOKMARK_DELETE_BOOKMARK_CLICKED.pixelName to PixelParameter.removeAtb(),
            EDIT_BOOKMARK_DELETE_BOOKMARK_CONFIRMED.pixelName to PixelParameter.removeAtb(),
            EDIT_BOOKMARK_DELETE_BOOKMARK_CANCELLED.pixelName to PixelParameter.removeAtb(),
            BOOKMARK_MENU_ADD_FAVORITE_CLICKED.pixelName to PixelParameter.removeAtb(),
            BOOKMARK_MENU_EDIT_BOOKMARK_CLICKED.pixelName to PixelParameter.removeAtb(),
            BOOKMARK_MENU_REMOVE_FAVORITE_CLICKED.pixelName to PixelParameter.removeAtb(),
            BOOKMARK_MENU_DELETE_BOOKMARK_CLICKED.pixelName to PixelParameter.removeAtb(),
        )
    }
}
