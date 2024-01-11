package com.example.maintenancespace.services;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.maintenancespace.MainActivity;
import com.example.maintenancespace.ui.gps.GpsViewModel;

import java.util.function.Consumer;

public class TripService extends Service {
    public static String ACTIVE_EXTRA = "ACTIVE_EXTRA";
    public Context context = this;
    private LocationManager locationManager;

    private Location previousLocation = null;
    private GpsViewModel gpsViewModel;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        super.onDestroy();
        gpsViewModel.setIsTripServiceRunning(false);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();
        gpsViewModel = new ViewModelProvider(MainActivity.activity).get(GpsViewModel.class);

        LocationListener locationListener = location -> {

        };
        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_REDELIVER_INTENT;
        }
        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, getApplication().getMainExecutor(), new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                TripService.this.previousLocation = location;
            }
        });
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 20, locationListener);
        intent.putExtra(ACTIVE_EXTRA, true);
        gpsViewModel.setIsTripServiceRunning(true);

        Toast.makeText(this, "Service", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }
}
