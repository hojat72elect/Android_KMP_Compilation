

package com.duckduckgo.duckplayer.impl.di

import android.content.Context
import android.content.res.AssetManager
import android.webkit.MimeTypeMap
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class DuckPlayerModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideMimeTypeMap(): MimeTypeMap {
        return MimeTypeMap.getSingleton()
    }

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideAssetManager(context: Context): AssetManager {
        return context.assets
    }
}
