

package com.duckduckgo.savedsites.impl.sync

import com.duckduckgo.di.scopes.*
import com.duckduckgo.savedsites.api.models.*
import com.duckduckgo.savedsites.impl.FavoritesDisplayModeSettingsRepository
import com.duckduckgo.savedsites.store.FavoritesDisplayMode.NATIVE
import com.duckduckgo.savedsites.store.FavoritesDisplayMode.UNIFIED
import com.duckduckgo.savedsites.store.SavedSitesEntitiesDao
import com.duckduckgo.savedsites.store.SavedSitesRelationsDao
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

interface SavedSitesFormFactorSyncMigration {
    fun onFormFactorFavouritesEnabled()
    fun onFormFactorFavouritesDisabled()
}

@ContributesBinding(AppScope::class)
class RealSavedSitesFormFactorSyncMigration @Inject constructor(
    private val savedSitesEntitiesDao: SavedSitesEntitiesDao,
    private val savedSitesRelationsDao: SavedSitesRelationsDao,
    private val favoritesFormFactorSettings: FavoritesDisplayModeSettingsRepository,
) : SavedSitesFormFactorSyncMigration {
    override fun onFormFactorFavouritesEnabled() {
        Timber.d("Sync-Bookmarks: syncEnabled creating FFS folders")
        savedSitesEntitiesDao.createFormFactorFavoriteFolders()
        savedSitesRelationsDao.cloneFolder(
            SavedSitesNames.FAVORITES_ROOT,
            SavedSitesNames.FAVORITES_MOBILE_ROOT,
        )
    }

    override fun onFormFactorFavouritesDisabled() {
        Timber.d("Sync-Bookmarks: syncDisabled removing FFS folders and migrating favorites")
        when (favoritesFormFactorSettings.favoritesDisplayMode) {
            NATIVE -> {
                savedSitesRelationsDao.migrateNativeFavoritesAsNewRoot()
            }
            UNIFIED -> {
                savedSitesRelationsDao.migrateUnifiedFavoritesAsNewRoot()
            }
        }
        savedSitesEntitiesDao.removeFormFactorFavoriteFolders()
        favoritesFormFactorSettings.favoritesDisplayMode = NATIVE
        Timber.d("Sync-Bookmarks: favoriteFormFactor changed to NATIVE")
    }
}
