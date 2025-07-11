

package com.duckduckgo.app.di

import dagger.Module
import dagger.Provides
import java.text.NumberFormat
import javax.inject.Named

@Module
object FormatterModule {

    @Provides
    @Named("numberFormatterWithSeparator")
    fun providesNumberFormatter(): NumberFormat {
        return NumberFormat.getInstance().also {
            it.maximumFractionDigits = 1
        }
    }
}
