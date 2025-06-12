package com.amaze.fileutilities.utilis

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.amaze.fileutilities.home_page.database.AppDatabase
import java.time.ZonedDateTime
import java.util.Date

/**
 * A [CoroutineWorker] to insert the size of each app into the database and
 * delete entries that are older than [PreferencesConstants.MAX_LARGE_SIZE_DIFF_APPS_DAYS] days.
 */
class QueryAppSizeWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            !isUsageStatsPermissionGranted(this.applicationContext)
        ) {
            return Result.failure()
        }

        val appStorageStatsDao = AppDatabase.getInstance(applicationContext).appStorageStatsDao()
        val packageManager = applicationContext.packageManager
        // Get all currently installed apps
        val allApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        } else {
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }
        // Find the current size of each app and store them in the database
        for (appInfo in allApps) {
            val currentSize = Utils.findApplicationInfoSize(applicationContext, appInfo)
            val timestamp = Date.from(ZonedDateTime.now().toInstant())
            appStorageStatsDao.insert(appInfo.packageName, timestamp, currentSize)
        }

        // Delete all AppStorageStats entries that are older than MAX_LARGE_SIZE_DIFF_APPS_DAYS
        val minDate = Date.from(
            ZonedDateTime
                .now()
                .minusDays(PreferencesConstants.MAX_LARGE_SIZE_DIFF_APPS_DAYS.toLong())
                .toInstant()
        )
        appStorageStatsDao.deleteOlderThan(minDate)

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun isUsageStatsPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.checkUsageStatsPermission(context)
        } else {
            Utils.getAppsUsageStats(context, 30).isNotEmpty()
        }
    }

    companion object {
        const val NAME: String = "query_app_size_worker"
    }
}
