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
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.models.events.MaintenanceEventStatus;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

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

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel(CHANNEL_ID, notificationManager);

        checkAndRequestNotificationPermission(getApplicationContext());

        MaintenanceEventController.fetchAllByUserId(UserController.getCurrentUser().getUid(), new MaintenanceEventController.MaintenanceEventListener() {
            @Override
            public void onEventFetched(MaintenanceEventModel event) {
                //scheduleNotification(10000, CHANNEL_ID, String.valueOf(NOTIFICATION_ID), carString, eventStatus, eventName);
            }

            @Override
            public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {
                for (MaintenanceEventModel event : events) {
                    Timestamp eventTimestamp = event.getDate();
                    long eventTimeMillis = eventTimestamp.toDate().getTime();
                    long currentTimeMillis = System.currentTimeMillis();
                    long timeDifferenceMillis = eventTimeMillis - currentTimeMillis;
                    MaintenanceEventStatus status;
                    if(timeDifferenceMillis>0){
                        status = MaintenanceEventStatus.FUTURE;
                    }
                    if(timeDifferenceMillis<=0){
                        status = MaintenanceEventStatus.PAST_DUE;
                    }
                    else{
                        status = MaintenanceEventStatus.OTHER;
                    }

                    MaintenanceEventStatus finalStatus = status;
                    CarController.fetchCarById(event.getCarId(), new CarController.CarListener() {
                        @Override
                        public void onCarFetched(CarModel car) {
                            String carString = car.toString();

                            scheduleNotification(timeDifferenceMillis, CHANNEL_ID, event.getId(), carString, finalStatus, event.getName().toString());
                        }

                        @Override
                        public void onCarsFetched(ArrayList<CarModel> cars) {

                        }

                        @Override
                        public void onDailyDistanceUpdate(String carId) {

                        }

                        @Override
                        public void onCreation(String docId) {

                        }

                        @Override
                        public void onDelete(String docId) {

                        }

                        @Override
                        public void onUpdate(String docId) {

                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Handle the failure case
                        }
                    });
                }
            }

            @Override
            public void onCreation(String docId) {

            }

            @Override
            public void onDelete(String docId) {

            }

            @Override
            public void onUpdate(String docId) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void scheduleNotification(long delay, String CHANNEL_ID, String NOTIFICATION_ID,
                                     String carString, MaintenanceEventStatus eventStatus, String eventName) {

        Intent intent = new Intent(this, NotificationReceiver.class);

        intent.putExtra("CHANNEL_ID", CHANNEL_ID);
        intent.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);
        intent.putExtra("carString", carString);
        intent.putExtra("eventStatus", convertMaintenanceEventStatus(eventStatus));
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

    public void rescheduleNotification(Context context, String notificationId, int newDelayMillis, String CHANNEL_ID,
                                       String carString, MaintenanceEventStatus eventStatus, String eventName) {

        cancelNotification(context, notificationId);
        scheduleNotification(newDelayMillis, CHANNEL_ID, notificationId, carString, eventStatus, eventName);
    }

    public void cancelNotification(Context context, String notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(notificationId));
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

    private String convertMaintenanceEventStatus(MaintenanceEventStatus status) {
        switch (status) {
            case FUTURE:
                return "scheduled";
            case PAST_DUE:
                return "past due";
            default:
                return "unknown";
        }
    }
}