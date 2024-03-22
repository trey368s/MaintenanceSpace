package com.example.maintenancespace.ui.gps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GpsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isTripServiceRunning;

    private final MutableLiveData<Float> tripDistance;

    private final MutableLiveData<Long> tripTimer;

    public GpsViewModel() {
        isTripServiceRunning = new MutableLiveData<>(false);
        tripDistance = new MutableLiveData<>(0f);
        tripTimer = new MutableLiveData<>(0L);
    }

    public void setIsTripServiceRunning(Boolean isRunning) { isTripServiceRunning.setValue(isRunning); }
    public LiveData<Boolean> getIsTripServiceRunning() { return isTripServiceRunning; }

    public void setTripDistance(Float inTripDistance) { tripDistance.setValue(inTripDistance); }
    public LiveData<Float> getTripDistance() { return tripDistance; }

    public void setTripTimer(Long inTripTimer) { tripTimer.setValue(inTripTimer); }
    public LiveData<Long> getTripTimer() { return tripTimer; }
}