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
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.dailyMileage.DailyMileageModel;
import com.example.maintenancespace.ui.gps.GpsViewModel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TripService extends Service {
    private LocationManager locationManager;
    private String carId;

    private float tripDistanceInMeters = 0;

    private int PERMISSION_REQUEST_CODE = 100;

    private Location previousLocation = null;
    private GpsViewModel gpsViewModel;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateCarMileage(this.tripDistanceInMeters);
        gpsViewModel.setIsTripServiceRunning(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId) {
        super.onStartCommand(intent, flags, startId);
        this.carId = intent.getStringExtra("carId");
        gpsViewModel = new ViewModelProvider(MainActivity.activity).get(GpsViewModel.class);

        LocationListener locationListener = location -> {
            if (previousLocation != null) {
                float distance = this.previousLocation.distanceTo(location);
                this.tripDistanceInMeters += distance;
            }
            this.previousLocation = location;
        };

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] requiredPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            MainActivity.activity.requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE);

            stopSelf();
            return START_NOT_STICKY;
        }
        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, getApplication().getMainExecutor(), new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                TripService.this.previousLocation = location;
            }
        });
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 50, locationListener);
        gpsViewModel.setIsTripServiceRunning(true);

//        Toast.makeText(this, "Service", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    public void updateCarMileage(float distanceInMeters) {
        if (distanceInMeters > 0) {
            CarController.fetchCarById(this.carId, new CarController.CarListener() {
                @Override
                public void onCarFetched(CarModel car) {
                    ArrayList<DailyMileageModel> dailyMileage = car.getDailyMileageDays();

//                    CarController.updateDailyMileageByCarId(TripService.this.carId);
                }

                @Override
                public void onCarsFetched(ArrayList<CarModel> cars) {

                }

                @Override
                public void onDailyMileageUpdate(String carId) {

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
        }
    }
}
