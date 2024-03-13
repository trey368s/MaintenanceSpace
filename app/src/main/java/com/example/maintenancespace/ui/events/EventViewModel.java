package com.example.maintenancespace.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MaintenanceEventModel>> events;

    public EventViewModel() {
        events = new MutableLiveData<>(null);
    }

    public LiveData<ArrayList<MaintenanceEventModel>> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<MaintenanceEventModel> updatedEvents) { events.setValue(updatedEvents);}
}