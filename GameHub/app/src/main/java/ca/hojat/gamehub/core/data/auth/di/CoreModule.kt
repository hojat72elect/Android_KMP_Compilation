package ca.hojat.gamehub.core.data.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import ca.hojat.gamehub.core.data.auth.datastores.file.NewProtoOauthCredentials
import ca.hojat.gamehub.core.data.auth.datastores.file.ProtoOauthCredentialsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val AUTH_PREFERENCES_DATA_STORE_NAME = "auth.pb"

private val Context.authProtoDataStore by dataStore(
    fileName = AUTH_PREFERENCES_DATA_STORE_NAME,
    serializer = ProtoOauthCredentialsSerializer
)

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    fun provideAuthProtoDataStore(
        @ApplicationContext context: Context
    ): DataStore<NewProtoOauthCredentials> {
        return context.authProtoDataStore
    }
}
