

package com.duckduckgo.mobile.android.vpn.di

import android.content.Context
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.R.string
import com.duckduckgo.mobile.android.vpn.ui.AppBreakageCategory
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(ActivityScope::class)
object AppTrackerBreakageCategoriesModule {
    @Provides
    @AppTpBreakageCategories
    fun provideAppTrackerBreakageCategories(context: Context): List<AppBreakageCategory> {
        return mutableListOf(
            AppBreakageCategory("crashes", context.getString(string.atp_ReportBreakageCategoryCrashes)),
            AppBreakageCategory("messages", context.getString(string.atp_ReportBreakageCategoryMessages)),
            AppBreakageCategory("calls", context.getString(string.atp_ReportBreakageCategoryCalls)),
            AppBreakageCategory("uploads", context.getString(string.atp_ReportBreakageCategoryUploads)),
            AppBreakageCategory("downloads", context.getString(string.atp_ReportBreakageCategoryDownloads)),
            AppBreakageCategory("content", context.getString(string.atp_ReportBreakageCategoryContent)),
            AppBreakageCategory("connection", context.getString(string.atp_ReportBreakageCategoryConnection)),
            AppBreakageCategory("iot", context.getString(string.atp_ReportBreakageCategoryIot)),
        ).apply {
            shuffle()
            add(AppBreakageCategory("other", context.getString(string.atp_ReportBreakageCategoryOther)))
        }
    }
}
