

package com.duckduckgo.app.di

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(
    scope = AppScope::class,
    replaces = [JobsModule::class],
)
class StubJobSchedulerModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun providesJobScheduler(): JobScheduler {
        return object : JobScheduler() {
            override fun enqueue(
                job: JobInfo,
                work: JobWorkItem,
            ): Int = RESULT_SUCCESS

            override fun schedule(job: JobInfo): Int = RESULT_SUCCESS

            override fun cancel(jobId: Int) {}

            override fun cancelAll() {}

            override fun getAllPendingJobs(): MutableList<JobInfo> = mutableListOf()

            override fun getPendingJob(jobId: Int): JobInfo? = null
        }
    }
}
