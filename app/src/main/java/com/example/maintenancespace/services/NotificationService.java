package com.example.maintenancespace.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.example.maintenancespace.MainActivity;
import com.example.maintenancespace.NotificationReceiver;

public class NotificationService extends Service {

    private int PERMISSION_REQUEST_CODE = 100;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!checkAndRequestNotificationPermission(getApplicationContext())) {
            stopSelf();
            return START_NOT_STICKY;
        }

        String CHANNEL_ID = "UpdateChannel";
        NotificationManager notificationManager;

        int NOTIFICATION_ID = 1;
        String carString = "2005 Silver Buick LeSabre";
        String eventStatus = "approaching designated mileage"; //could be overdue
        String eventName = "oil change";

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(CHANNEL_ID, notificationManager);

        checkAndRequestNotificationPermission(getApplicationContext());

        scheduleNotification(10000, CHANNEL_ID, NOTIFICATION_ID, carString, eventStatus, eventName);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void scheduleNotification(int delay, String CHANNEL_ID, int NOTIFICATION_ID,
                                     String carString, String eventStatus, String eventName) {

        Intent intent = new Intent(this, NotificationReceiver.class);

        intent.putExtra("CHANNEL_ID", CHANNEL_ID);
        intent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);
        intent.putExtra("carString", carString);
        intent.putExtra("eventStatus", eventStatus);
        intent.putExtra("eventName", eventName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rescheduleNotification(Context context, int notificationId, int newDelayMillis, String CHANNEL_ID,
                                       String carString, String eventStatus, String eventName) {

        cancelNotification(context, notificationId);
        scheduleNotification(newDelayMillis, CHANNEL_ID, notificationId, carString, eventStatus, eventName);
    }

    public void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    private void createNotificationChannel(String CHANNEL_ID, NotificationManager notificationManager) {
        CharSequence channelName = "Maintenance Updates";
        String channelDescription = "Notification channel for updates about upcoming service events.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        channel.setDescription(channelDescription);

        notificationManager.createNotificationChannel(channel);
    }

    private boolean checkAndRequestNotificationPermission(Context context) {
        boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

        if (!areNotificationsEnabled) {
            String[] requiredPermissions = {Manifest.permission.POST_NOTIFICATIONS};
            MainActivity.activity.requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE);
            areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        return areNotificationsEnabled;
    }
}
