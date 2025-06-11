package ca.hojat.gamehub.core.data.api.igdb.common.di

import ca.hojat.gamehub.core.data.api.igdb.common.IgdbConstantsProvider
import ca.hojat.gamehub.core.data.api.igdb.common.ProdIgdbConstantsProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IgdbConstantsModule {

    @Binds
    fun bindIgdbConstantsProvider(binding: ProdIgdbConstantsProvider): IgdbConstantsProvider
}
