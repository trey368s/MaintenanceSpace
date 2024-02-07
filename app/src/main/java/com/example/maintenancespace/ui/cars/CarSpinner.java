package com.example.maintenancespace.ui.cars;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

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
        CarController.fetchAllCarsByUserId(UserController.getCurrentUser().getUid(), new CarController.CarListener() {
            @Override
            public void onCarFetched(CarModel car) {

            }

            @Override
            public void onCarsFetched(ArrayList<CarModel> cars) {
                ArrayAdapter carOptions = new ArrayAdapter(context, android.R.layout.simple_spinner_item, cars);
                CarSpinner.this.setAdapter(carOptions);
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
