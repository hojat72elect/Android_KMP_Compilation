

package com.duckduckgo.app.di

import android.content.Context
import com.duckduckgo.app.global.db.AppDatabase
import com.duckduckgo.app.statistics.api.*
import com.duckduckgo.app.statistics.store.PendingPixelDao
import com.duckduckgo.common.utils.device.ContextDeviceInfo
import com.duckduckgo.common.utils.device.DeviceInfo
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object StatisticsModule {

    @Provides
    fun offlinePixelSender(
        offlinePixels: DaggerSet<OfflinePixel>,
    ): OfflinePixelSender = OfflinePixelSender(offlinePixels)

    @Provides
    fun deviceInfo(context: Context): DeviceInfo = ContextDeviceInfo(context)

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun pixelDao(database: AppDatabase): PendingPixelDao {
        return database.pixelDao()
    }
}
