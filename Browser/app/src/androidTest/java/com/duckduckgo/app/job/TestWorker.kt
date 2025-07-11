

package com.duckduckgo.app.job

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TestWorker(
    context: Context,
    parameters: WorkerParameters,
) : Worker(context, parameters) {
    override fun doWork(): Result {
        return Result.success()
    }
}
