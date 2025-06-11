package ca.hojat.gamehub.core.data.api.igdb.common.di

import ca.hojat.gamehub.core.data.api.igdb.common.ProdTwitchConstantsProvider
import ca.hojat.gamehub.core.data.api.igdb.common.TwitchConstantsProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TwitchConstantsModule {

    @Binds
    fun bindTwitchConstantsProvider(binding: ProdTwitchConstantsProvider): TwitchConstantsProvider
}
