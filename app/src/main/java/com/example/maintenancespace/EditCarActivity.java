package com.example.maintenancespace;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.databinding.ActivityEditCarBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.cars.CarFragment;
import com.example.maintenancespace.ui.cars.CarListItemFragment;
import com.example.maintenancespace.ui.cars.CarViewModel;
import com.example.maintenancespace.ui.events.EventFragment;
import com.example.maintenancespace.ui.events.EventViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class EditCarActivity extends AppCompatActivity {
    private ActivityEditCarBinding binding;

    private boolean validateForm(String vin, String make, String model, String trim, int year) {
        if(vin.isEmpty()) {
            return false;
        }
        if(make.isEmpty()) {
            return false;
        }
        if(model.isEmpty()) {
            return false;
        }
        if(trim.isEmpty()) {
            return false;
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String carId = getIntent().getExtras().getString(CarListItemFragment.CAR_ID_KEY);
        CarModel existingCar = getIntent().getExtras().getParcelable("CAR", CarModel.class);

        binding = ActivityEditCarBinding.inflate(getLayoutInflater());
        CarViewModel carsViewModel = new ViewModelProvider(MainActivity.viewModelOwner).get(CarViewModel.class);
        EventViewModel eventViewModel  = new ViewModelProvider(MainActivity.viewModelOwner).get(EventViewModel.class);
        ConstraintLayout root = binding.getRoot();
        setContentView(root);
        EditText editVinField = root.findViewById(R.id.editVin);
        EditText editMakeField = root.findViewById(R.id.editMake);
        EditText editModel = root.findViewById(R.id.editModel);
        EditText editTrim = root.findViewById(R.id.editTrim);
        EditText editYear = root.findViewById(R.id.editYear);

        editVinField.setText(existingCar.getVin());
        editMakeField.setText(existingCar.getMake());
        editModel.setText(existingCar.getModel());
        editTrim.setText(existingCar.getTrim());
        editYear.setText(Integer.toString(existingCar.getYear()));

        Button deleteCarButton = binding.deleteCarButton;
        deleteCarButton.setOnClickListener(v -> {
            CarController.deleteCarById(carId, new CarController.CarListener() {
                @Override
                public void onCarFetched(CarModel car) {

                }

                @Override
                public void onCarsFetched(ArrayList<CarModel> cars) {

                }

                @Override
                public void onDailyDistanceUpdate(String carId) {

                }

                @Override
                public void onCreation(String docId) {

                }

                @Override
                public void onDelete(String docId) {
                    ArrayList<CarModel> cars = carsViewModel.getCars().getValue();
                    ArrayList<MaintenanceEventModel> events = eventViewModel.getEvents().getValue();

                    for (int i = 0; i < events.size(); i++) {
                        if(events.get(i).getCarId().equals(carId)) {
                            events.remove(i);
                        }
                    }
                    eventViewModel.setEvents(events);

                    for (int i = 0; i < cars.size(); i++) {
                        if(cars.get(i).getId().equals(carId)) {
                            cars.remove(i);
                        }
                    }
                    carsViewModel.setCars(cars);
                    finish();
                }

                @Override
                public void onUpdate(String docId) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });

        Button saveCarButton = binding.saveCarEditButton;
        saveCarButton.setOnClickListener(v -> {
            String vin = editVinField.getText().toString();
            String make = editMakeField.getText().toString();
            String model = editModel.getText().toString();
            String trim = editTrim.getText().toString();
            int year = Integer.parseInt(editYear.getText().toString());

            ArrayList userIds = new ArrayList<String>();
            userIds.add(UserController.getCurrentUser().getUid());
            CarModel updatedCar = new CarModel(vin, make, model, trim, year, UserController.getCurrentUser().getUid(), userIds);

            if(validateForm(vin, make, model, trim, year)) {
                CarController.updateCarById(carId, updatedCar, new CarController.CarListener() {

                    @Override
                    public void onCarFetched(CarModel car) {

                    }

                    @Override
                    public void onCarsFetched(ArrayList<CarModel> cars) {

                    }

                    @Override
                    public void onDailyDistanceUpdate(String carId) {

                    }

                    @Override
                    public void onCreation(String docId) {
                    }

                    @Override
                    public void onDelete(String docId) {

                    }

                    @Override
                    public void onUpdate(String docId) {
                        ArrayList<CarModel> cars = carsViewModel.getCars().getValue();
                        updatedCar.setId(docId);

                        for (int i = 0; i < cars.size(); i++) {
                            if(cars.get(i).getId().equals(carId)) {
                                cars.set(i, updatedCar);
                            }
                        }
                        carsViewModel.setCars(cars);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("FAIL", e.toString());
                    }
                });
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
