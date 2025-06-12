package de.nulide.findmydevice.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;

import de.nulide.findmydevice.commands.CommandHandler;
import de.nulide.findmydevice.transports.SmsTransport;
import de.nulide.findmydevice.transports.Transport;


public class SmsService extends FmdJobService {

    private static final String TAG = SmsService.class.getSimpleName();

    private static final int JOB_ID = 107;

    private static final String DESTINATION = "dest";
    private static final String MESSAGE = "msg";
    private static final String SUBSCRIPTION_ID = "subscription-id";

    public static void scheduleJob(Context context, String destination, int subscriptionId, String message) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(DESTINATION, destination);
        bundle.putInt(SUBSCRIPTION_ID, subscriptionId);
        bundle.putString(MESSAGE, message);

        ComponentName serviceComponent = new ComponentName(context, SmsService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent)
                .setExtras(bundle);
        builder.setMinimumLatency(0);
        builder.setOverrideDeadline(0);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    public boolean onStartJob(JobParameters params) {
        super.onStartJob(params);

        String phoneNumber = params.getExtras().getString(DESTINATION);
        int subscriptionId = params.getExtras().getInt(SUBSCRIPTION_ID);
        String msg = params.getExtras().getString(MESSAGE);

        Transport<String> transport = new SmsTransport(this, phoneNumber, subscriptionId);
        CommandHandler<String> commandHandler = new CommandHandler<>(transport, this.getCoroutineScope(), this);
        commandHandler.execute(this, msg);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        super.onStopJob(params);
        return false;
    }
}
