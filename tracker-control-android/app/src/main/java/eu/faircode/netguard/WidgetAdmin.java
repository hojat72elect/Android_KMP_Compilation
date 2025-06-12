/*
 * This file is from NetGuard.
 *
 * NetGuard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NetGuard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NetGuard.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright © 2015–2020 by Marcel Bokhorst (M66B), Konrad
 * Kollnig (University of Oxford)
 */

package eu.faircode.netguard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Date;

public class WidgetAdmin extends ReceiverAutostart {
    private static final String TAG = "TrackerControl.Widget";

    public static final String INTENT_ON = "net.kollnig.missioncontrol.ON";
    public static final String INTENT_PAUSE = "net.kollnig.missioncontrol.PAUSE";
    public static final String INTENT_OFF = "net.kollnig.missioncontrol.OFF";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.i(TAG, "Received " + intent);
        Util.logExtras(intent);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Cancel set alarm
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(INTENT_ON);
        i.setPackage(context.getPackageName());
        PendingIntent pi = PendingIntentCompat.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if (INTENT_ON.equals(intent.getAction())
                || INTENT_OFF.equals(intent.getAction())
                || INTENT_PAUSE.equals(intent.getAction()))
            am.cancel(pi);

        // Vibrate
        Vibrator vs = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vs.hasVibrator())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vs.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            else
                vs.vibrate(50);

        try {
            if (INTENT_ON.equals(intent.getAction())
                    || INTENT_OFF.equals(intent.getAction())
                    || INTENT_PAUSE.equals(intent.getAction())) {
                boolean enabled = INTENT_ON.equals(intent.getAction());
                prefs.edit().putBoolean("enabled", enabled).apply();
                if (enabled)
                    ServiceSinkhole.start("widget", context);
                else
                    ServiceSinkhole.stop("widget", context, false);

                // Auto enable
                int auto = Integer.parseInt(prefs.getString("pause", "10"));
                if (!enabled && auto > 0 && INTENT_PAUSE.equals(intent.getAction())) {
                    Log.i(TAG, "Scheduling enabled after minutes=" + auto);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                        am.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + auto * 60 * 1000L, pi);
                    else
                        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, new Date().getTime() + auto * 60 * 1000L, pi);
                }
            }
        } catch (Throwable ex) {
            Log.e(TAG, ex.toString() + "\n" + Log.getStackTraceString(ex));
        }
    }
}
