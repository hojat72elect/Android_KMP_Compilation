

package com.duckduckgo.autofill.impl.securestorage.di

import android.content.Context
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.autofill.impl.securestorage.DerivedKeySecretFactory
import com.duckduckgo.autofill.impl.securestorage.RealDerivedKeySecretFactory
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.securestorage.store.RealSecureStorageKeyRepository
import com.duckduckgo.securestorage.store.SecureStorageKeyRepository
import com.duckduckgo.securestorage.store.keys.RealSecureStorageKeyStore
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import javax.inject.Named
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object SecureStorageModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesSecureStorageKeyStore(
        context: Context,
        @AppCoroutineScope coroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider,
        autofillFeature: AutofillFeature,
    ): SecureStorageKeyRepository =
        RealSecureStorageKeyRepository(
            RealSecureStorageKeyStore(context, coroutineScope, dispatcherProvider, autofillFeature),
        )
}

@Module
@ContributesTo(AppScope::class)
object SecureStorageKeyModule {
    @Provides
    @Named("DerivedKeySecretFactoryFor26Up")
    fun provideDerivedKeySecretFactoryFor26Up(): DerivedKeySecretFactory = RealDerivedKeySecretFactory()
}
