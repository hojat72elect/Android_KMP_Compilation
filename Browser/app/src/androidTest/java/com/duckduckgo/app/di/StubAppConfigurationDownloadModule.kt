

package com.duckduckgo.app.di

import com.duckduckgo.app.job.ConfigurationDownloader
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.reactivex.Completable

@Module
@ContributesTo(
    scope = AppScope::class,
    replaces = [AppConfigurationDownloaderModule::class],
)
class StubAppConfigurationDownloadModule {

    @Provides
    fun fakeAppConfigurationDownloader(): ConfigurationDownloader {
        return object : ConfigurationDownloader {
            override fun downloadTask(): Completable {
                return Completable.complete()
            }
        }
    }
}
