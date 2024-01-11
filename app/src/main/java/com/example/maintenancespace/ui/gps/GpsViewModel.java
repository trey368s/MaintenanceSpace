package com.example.maintenancespace.ui.gps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GpsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isTripServiceRunning;

    public GpsViewModel() {
        isTripServiceRunning = new MutableLiveData<>(false);
    }

    public void setIsTripServiceRunning(Boolean isRunning) { isTripServiceRunning.setValue(isRunning); }
    public LiveData<Boolean> getIsTripServiceRunning() { return isTripServiceRunning; }
}