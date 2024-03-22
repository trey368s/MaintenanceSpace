package com.example.maintenancespace.ui.cars;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.MainActivity;
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.models.cars.CarModel;

import java.util.ArrayList;

public class CarSpinner extends androidx.appcompat.widget.AppCompatSpinner {
    public CarSpinner(Context context) {
        super(context);
        this.setOptions(context);
    }

    public CarSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOptions(context);
    }

    public void setCar(String carId) {
        SpinnerAdapter arrayAdapter = this.getAdapter();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            CarModel car = (CarModel) arrayAdapter.getItem(i);
            if(car.getId().equals(carId)) {
                this.setSelection(i);
            }
        }
    }

    private void setOptions(Context context) {
        CarViewModel carViewModel = new ViewModelProvider(MainActivity.viewModelOwner).get(CarViewModel.class);

        ArrayAdapter carOptions = new ArrayAdapter(context, android.R.layout.simple_spinner_item, carViewModel.getCars().getValue());
        CarSpinner.this.setAdapter(carOptions);
    }

}
