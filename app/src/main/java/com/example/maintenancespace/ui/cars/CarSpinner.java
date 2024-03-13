package com.example.maintenancespace.ui.cars;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

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

    private void setOptions(Context context) {
        CarViewModel carViewModel = new ViewModelProvider(MainActivity.viewModelOwner).get(CarViewModel.class);

        ArrayAdapter carOptions = new ArrayAdapter(context, android.R.layout.simple_spinner_item, carViewModel.getCars().getValue());
        CarSpinner.this.setAdapter(carOptions);
    }

}
