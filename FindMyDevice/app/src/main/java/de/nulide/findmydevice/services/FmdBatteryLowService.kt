package de.nulide.findmydevice.services

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import de.nulide.findmydevice.receiver.BatteryLowReceiver

class FmdBatteryLowService : FmdJobService() {

    companion object {
        const val JOB_ID: Int = 110

        @JvmStatic
        fun scheduleJobNow(context: Context) {
            if(!isRunning(context)) {
                val serviceComponent = ComponentName(context, FmdBatteryLowService::class.java)
                val builder = JobInfo.Builder(JOB_ID, serviceComponent)
                builder.setMinimumLatency(0)
                builder.setOverrideDeadline(1000)
                builder.setPersisted(true)
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                val jobScheduler = context.getSystemService(JobScheduler::class.java)
                jobScheduler.schedule(builder.build())
            }
        }

        @JvmStatic
        fun stopJobNow(context: Context){
            val jobScheduler = context.getSystemService(JobScheduler::class.java)
            jobScheduler.cancel(JOB_ID)
        }

        @JvmStatic
        fun isRunning(context: Context): Boolean{
            val jobScheduler = context.getSystemService(JobScheduler::class.java)
            for (jobInfo in jobScheduler.allPendingJobs){
                if (jobInfo.id == JOB_ID){
                    return true
                }
            }
            return false
        }
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        super.onStartJob(params)
        val filter = IntentFilter(Intent.ACTION_BATTERY_LOW)
        val batteryLowReceiver = BatteryLowReceiver()
        registerReceiver(batteryLowReceiver, filter)
        return true
    }
}