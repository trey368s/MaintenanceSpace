package com.example.maintenancespace;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.maintenancespace.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_cars, R.id.navigation_events, R.id.navigation_reports)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

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

    private void checkAndRequestNotificationPermission(Context context) {
        boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

        if (!areNotificationsEnabled) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
    }
}
