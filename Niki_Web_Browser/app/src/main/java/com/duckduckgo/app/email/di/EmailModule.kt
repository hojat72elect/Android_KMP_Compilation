

package com.duckduckgo.app.email.di

import android.content.Context
import com.duckduckgo.app.email.db.EmailDataStore
import com.duckduckgo.app.email.db.EmailEncryptedSharedPreferences
import com.duckduckgo.app.statistics.pixels.Pixel
import dagger.Module
import dagger.Provides

@Module
class EmailModule {

    @Provides
    fun providesEmailDataStore(
        context: Context,
        pixel: Pixel,
    ): EmailDataStore {
        return EmailEncryptedSharedPreferences(context, pixel)
    }
}
