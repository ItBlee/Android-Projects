package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String turnMusic = intent.getStringExtra("turnMusic");

        //hiển thị thanh thông báo
        if (turnMusic.equals("on")) {
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            notificationHelper.getManager().notify(1, nb.build());
        }

        Intent myIntent = new Intent(context, AlertMusic.class);
        myIntent.putExtra("turnMusic",turnMusic);
        context.startService(myIntent);
    }
}