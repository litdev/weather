package com.example.userx.weather.service;

/**
 * Created by userx on 28/01/17.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.userx.weather.model.DataManager;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "service.AlarmReceiver";

    private static final String INTENT_ACTION = "com.example.userx.weather.ALARM";

    private static final long INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(INTENT_ACTION)) {

            Log.d(TAG, "Alarm has been received");

            // Notifications.blankNotification(context, "ALARM");

            DataManager.getInstance().initialize(context);
            DataManager.getInstance().updateData();
        }

    }// onReceive

    public synchronized static boolean startAlarm(Context context) {

        if (context == null) return false;

        // If the alarm is not set yet
        if ((PendingIntent.getBroadcast(context, 0, new Intent(INTENT_ACTION),
                PendingIntent.FLAG_NO_CREATE) == null)) {

            Intent recIntente = new Intent(INTENT_ACTION);
            PendingIntent recPendingIntent = PendingIntent.getBroadcast(context, 0, recIntente, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            manager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + INTERVAL,
                    INTERVAL,
                    recPendingIntent);

            Notifications.blankNotification(context, "Alarm has successfully been set");

            return true;
        }

        return false;
    }// startAlarm

}// AlarmReceiver