

package com.duckduckgo.app.di

import android.app.job.JobScheduler
import android.content.Context
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object JobsModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun providesJobScheduler(context: Context): JobScheduler {
        return context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }
}
