

package com.duckduckgo.app.di

import com.duckduckgo.app.trackerdetection.api.ActionJsonAdapter
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.httpsupgrade.impl.HttpsFalsePositivesJsonAdapter
import com.duckduckgo.privacy.config.impl.network.JSONObjectAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
object JsonModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun moshi(): Moshi = Moshi.Builder()
        .add(ActionJsonAdapter())
        // FIXME we should not access HttpsFalsePositivesJsonAdapter directly here because it's in impl module
        .add(HttpsFalsePositivesJsonAdapter())
        .add(JSONObjectAdapter())
        .build()
}
