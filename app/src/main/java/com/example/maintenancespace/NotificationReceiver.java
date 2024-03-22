package com.example.maintenancespace;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = intent.getStringExtra("CHANNEL_ID");
        int notificationId = intent.getIntExtra("NOTIFICATION_ID",-1);
        String carString = intent.getStringExtra("carString");
        String eventStatus = intent.getStringExtra("eventStatus");
        String eventName = intent.getStringExtra("eventName");

        // Code to show the notification
        showNotification(context, (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE),
                channelId, notificationId, carString, eventStatus, eventName);
    }

    public void showNotification(Context context, NotificationManager manager, String CHANNEL_ID, int NOTIFICATION_ID,
                                 String carString, String eventStatus, String eventName) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder builder = null;

        builder = new Notification.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Service Update - MaintenanceSpace")
                .setContentText("Your " + carString + " is " + eventStatus + " for a " + eventName + ".")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification;
        notification = builder.build();
        manager.notify(NOTIFICATION_ID, notification);
    }
}
