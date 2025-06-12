package de.nulide.findmydevice.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import java.util.concurrent.ThreadLocalRandom;

import de.nulide.findmydevice.commands.CommandHandler;
import de.nulide.findmydevice.data.Settings;
import de.nulide.findmydevice.data.SettingsRepository;
import de.nulide.findmydevice.transports.FmdServerTransport;
import de.nulide.findmydevice.transports.Transport;
import de.nulide.findmydevice.utils.FmdLogKt;
import de.nulide.findmydevice.utils.NetworkUtils;
import kotlin.Unit;


/**
 * Uploads the location at regular intervals in the background
 */
public class ServerLocationUploadService extends FmdJobService {

    private static final String TAG = ServerLocationUploadService.class.getSimpleName();

    private static final int JOB_ID = 108; // for recurring jobs only

    private static final String EXTRA_RECURRING = "EXTRA_RECURRING";

    private boolean recurring = false;
    private SettingsRepository settings;

    public static void scheduleJob(Context context, long delayMinutes) {
        scheduleJob(context, delayMinutes, true);
    }

    public static void scheduleJob(Context context, long delayMinutes, boolean recurring) {
        SettingsRepository settings = SettingsRepository.Companion.getInstance(context);
        if (((Integer) settings.get(Settings.SET_FMDSERVER_LOCATION_TYPE)) == 3) {
            // user requested NOT to upload any location at regular intervals
            FmdLogKt.log(context).d(TAG, "Not scheduling job. Reason: user requested no upload");
            return;
        }

        int jobId = JOB_ID;
        if (!recurring) {
            // Use a random job id for non-recurring, on-demand jobs.
            // Otherwise, if they have the same ID, Android will treat them as one,
            // and e.g., share the EXTRA_RECURRING, which causes issues.
            jobId = ThreadLocalRandom.current().nextInt();
        }

        ComponentName serviceComponent = new ComponentName(context, ServerLocationUploadService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, serviceComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        // We cannot use setPeriodic() because that only works for periods >= 15 mins
        // builder.setPeriodic(intervalMinutes * 60 * 1000);
        // Instead we use setMinimumLatency().
        builder.setMinimumLatency(delayMinutes * 60 * 1000);

        // Force the job to run within a 2 minute window, even if its constraints aren't satisfied.
        // This is to (hopefully) improve reliability and timeliness.
        // We can abort the job later if there is no network connection.
        builder.setOverrideDeadline(((delayMinutes + 2) * 60 * 1000));

        builder.setPersisted(true);

        PersistableBundle extras = new PersistableBundle();
        extras.putBoolean(EXTRA_RECURRING, recurring);
        builder.setExtras(extras);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    public static void cancelJob(Context context) {
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.cancel(JOB_ID);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        super.onStartJob(params);
        FmdLogKt.log(this).d(TAG, "Starting background upload job with jobId=" + params.getJobId());

        PersistableBundle extras = params.getExtras();
        recurring = extras.getBoolean(EXTRA_RECURRING);

        settings = SettingsRepository.Companion.getInstance(this);

        if (!settings.serverAccountExists()) {
            FmdLogKt.log(this).i(TAG, "No account, stopping and cancelling job.");
            cancelJob(this);
            return false;
        }

        if (!NetworkUtils.isNetworkAvailable(this)) {
            FmdLogKt.log(this).i(TAG, "No network connection, stopping job.");
            jobFinished();
            return false;
        }

        // Skip recurring uploads if the last upload was recently.
        // Non-recurring, on-demand uploads are always handled.
        if (recurring) {
            long now = System.currentTimeMillis();
            long lastUploadTimeMillis = ((Number) settings.get(Settings.SET_LAST_KNOWN_LOCATION_TIME)).longValue();
            long uploadIntervalMillis = ((int) settings.get(Settings.SET_FMDSERVER_UPDATE_TIME)) * 60 * 1000L;
            if (lastUploadTimeMillis + uploadIntervalMillis / 2 > now) {
                FmdLogKt.log(this).i(TAG, "Skipping upload, last upload was recent");
                jobFinished();
                return false;
            }
        }

        Transport<Unit> transport = new FmdServerTransport(this, "Regular Background Upload");
        CommandHandler<Unit> commandHandler = new CommandHandler<>(transport, this.getCoroutineScope(), this, false);

        String locateCommand = settings.get(Settings.SET_FMD_COMMAND) + " locate";
        switch ((Integer) settings.get(Settings.SET_FMDSERVER_LOCATION_TYPE)) {
            case 0:
                locateCommand += " gps";
                break;
            case 1:
                locateCommand += " cell";
                break;
            case 2:
                // no need to change the command
                break;
            case 3:
                // we should not be here...
                return false;
        }
        commandHandler.execute(this, locateCommand);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        super.onStopJob(params);
        // request retry (instead of scheduleNextOccurrence())
        return true;
    }

    @Override
    public void jobFinished() {
        super.jobFinished();
        if (recurring) {
            scheduleNextOccurrence();
        }
    }

    private void scheduleNextOccurrence() {
        FmdLogKt.log(this).d(TAG, "job stopped, rescheduling");

        long intervalMinutes = ((Integer) settings.get(Settings.SET_FMDSERVER_UPDATE_TIME)).longValue();
        if (intervalMinutes <= 0) {
            FmdLogKt.log(this).i(TAG, "Raising interval from " + intervalMinutes + " mins to 1 min");
            intervalMinutes = 1;
        }

        scheduleJob(this, intervalMinutes);
    }
}
