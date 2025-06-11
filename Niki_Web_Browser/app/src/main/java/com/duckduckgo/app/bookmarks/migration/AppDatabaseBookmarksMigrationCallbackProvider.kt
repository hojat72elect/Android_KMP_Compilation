

package com.duckduckgo.app.bookmarks.migration

import androidx.room.RoomDatabase
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.appbuildconfig.api.*
import com.duckduckgo.common.utils.DefaultDispatcherProvider
import dagger.Lazy

class AppDatabaseBookmarksMigrationCallbackProvider constructor(
    private val appDatabaseProvider: Lazy<AppDatabase>,
    private val appBuildConfig: AppBuildConfig,
) {
    fun provideCallbacks(): RoomDatabase.Callback {
        return AppDatabaseBookmarksMigrationCallback(appDatabaseProvider, DefaultDispatcherProvider(), appBuildConfig)
    }
}
