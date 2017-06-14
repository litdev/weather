package com.example.userx.weather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by userx on 31/01/17.
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            AlarmReceiver.startAlarm(context);
        }
    }// onReceive

}// DeviceBootReceiver
