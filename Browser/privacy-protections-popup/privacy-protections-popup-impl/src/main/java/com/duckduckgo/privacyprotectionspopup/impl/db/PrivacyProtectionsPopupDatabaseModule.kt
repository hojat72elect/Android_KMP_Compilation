

package com.duckduckgo.privacyprotectionspopup.impl.db

import android.content.Context
import androidx.room.Room
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
class PrivacyProtectionsPopupDatabaseModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesPrivacyProtectionsPopupDatabase(context: Context): PrivacyProtectionsPopupDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = PrivacyProtectionsPopupDatabase::class.java,
                name = "privacy_protections_popup.db",
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providePopupDismissDomainsDao(db: PrivacyProtectionsPopupDatabase): PopupDismissDomainsDao =
        db.popupDismissDomainDao()
}
