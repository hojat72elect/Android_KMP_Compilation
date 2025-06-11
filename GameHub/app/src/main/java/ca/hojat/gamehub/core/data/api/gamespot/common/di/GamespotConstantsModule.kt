package ca.hojat.gamehub.core.data.api.gamespot.common.di

import ca.hojat.gamehub.core.data.api.gamespot.common.GamespotConstantsProvider
import ca.hojat.gamehub.core.data.api.gamespot.common.ProdGamespotConstantsProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface GamespotConstantsModule {

    @Binds
    fun bindGamespotConstantsProvider(binding: ProdGamespotConstantsProvider): GamespotConstantsProvider
}
