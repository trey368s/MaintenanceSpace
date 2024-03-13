package com.example.maintenancespace.services;

import android.Manifest;
import android.app.Service;
import android.content.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.MainActivity;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.dailyDistance.DailyDistanceModel;
import com.example.maintenancespace.ui.cars.CarViewModel;
import com.example.maintenancespace.ui.events.EventViewModel;
import com.example.maintenancespace.ui.gps.GpsViewModel;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TripService extends Service {
    private LocationManager locationManager;

    private float tripDistanceInMeters = 0;

    private String carId;

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

        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, getApplication().getMainExecutor(), new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                TripService.this.tripDistanceInMeters += TripService.this.previousLocation.distanceTo(location);
                TripService.this.previousLocation = location;
                updateCarDistance(TripService.this.tripDistanceInMeters);
                gpsViewModel.setIsTripServiceRunning(false);
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId) {
        super.onStartCommand(intent, flags, startId);
        this.carId = intent.getStringExtra("CAR_ID");
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

        return START_STICKY;
    }

    public void updateCarDistance(float distanceInMeters) {
        if (distanceInMeters > 0) {
            CarViewModel viewModel = new ViewModelProvider(MainActivity.viewModelOwner).get(CarViewModel.class);
            ArrayList<CarModel> cars = viewModel.getCars().getValue();
            CarModel selectedCar = null;
            for(int i = 0; i < cars.size(); i++) {
                CarModel c = cars.get(i);
                if (c.getId().equals(carId)) {
                    selectedCar = c;
                }
            }

            if(selectedCar == null) return;

            ArrayList<DailyDistanceModel> dailyDistance = selectedCar.getDailyDistanceDays();
            ArrayList<DailyDistanceModel> updatedDailyDistance = DailyDistanceModel.createUpdatedDailyDistance(distanceInMeters, dailyDistance);
            selectedCar.setDailyDistanceDays(updatedDailyDistance);


            CarController.updateDailyDistanceByCarId(selectedCar.getId(), selectedCar.getDailyDistanceDays(), new CarController.CarListener() {
                @Override
                public void onCarFetched(CarModel car) {

                }

                @Override
                public void onCarsFetched(ArrayList<CarModel> cars) {

                }

                @Override
                public void onDailyDistanceUpdate(String carId) {
                    viewModel.setCars(cars);
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
