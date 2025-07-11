

package com.duckduckgo.privacy.dashboard.impl.di

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.dashboard.impl.ui.PrivacyDashboardHybridViewModel.RequestState
import com.duckduckgo.privacy.dashboard.impl.ui.PrivacyDashboardHybridViewModel.RequestState.Allowed
import com.duckduckgo.privacy.dashboard.impl.ui.PrivacyDashboardHybridViewModel.RequestState.Blocked
import com.duckduckgo.privacy.dashboard.impl.ui.ScreenKind
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Named

@Module
@ContributesTo(AppScope::class)
object JsonModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    @Named("privacyDashboard")
    fun moshi(moshi: Moshi): Moshi {
        return moshi.newBuilder()
            .add(
                PolymorphicJsonAdapterFactory.of(RequestState::class.java, "state")
                    .withSubtype(Blocked::class.java, "blocked")
                    .withSubtype(Allowed::class.java, "allowed"),
            )
            .add(ScreenKindJsonAdapter())
            .build()
    }
}

class ScreenKindJsonAdapter {
    @FromJson
    fun fromJson(value: String): ScreenKind? =
        ScreenKind.entries.find { it.value == value }

    @ToJson
    fun toJson(screen: ScreenKind?): String? =
        screen?.value
}
