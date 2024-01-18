package com.example.maintenancespace.ui.cars;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.maintenancespace.models.cars.CarModel;

import java.util.ArrayList;

public class CarViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<CarModel>> cars;

    public CarViewModel() {
        cars = new MutableLiveData<>(null);
    }

    public LiveData<ArrayList<CarModel>> getCars() {
        return cars;
    }

    public void setCars(ArrayList<CarModel> updatedCars) { cars.setValue(updatedCars);}
}