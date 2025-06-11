

package com.duckduckgo.app.di

import com.duckduckgo.app.global.file.AndroidFileDeleter
import com.duckduckgo.app.global.file.FileDeleter
import com.duckduckgo.common.utils.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module
object FileModule {

    @Provides
    fun providesFileDeleter(dispatchers: DispatcherProvider): FileDeleter {
        return AndroidFileDeleter(dispatchers)
    }
}
