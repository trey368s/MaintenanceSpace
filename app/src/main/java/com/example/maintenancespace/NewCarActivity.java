package com.example.maintenancespace;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.databinding.ActivityNewCarBinding;
import com.example.maintenancespace.databinding.ActivityNewMaintenanceEventBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.cars.CarFragment;
import com.example.maintenancespace.utilities.TimeHelpers;
import com.google.firebase.Timestamp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class NewCarActivity extends AppCompatActivity {
    public static WeakReference<CarFragment> carFragmentWeakReference;
    private ActivityNewCarBinding binding;


    public static void updateCarFragment(CarFragment fragment) {
        carFragmentWeakReference = new WeakReference(fragment);
    }

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

        binding = ActivityNewCarBinding.inflate(getLayoutInflater());
        ConstraintLayout root = binding.getRoot();
        setContentView(root);
        EditText editVinField = root.findViewById(R.id.editVin);
        EditText editMakeField = root.findViewById(R.id.editMake);
        EditText editModel = root.findViewById(R.id.editModel);
        EditText editTrim = root.findViewById(R.id.editTrim);
        EditText editYear = root.findViewById(R.id.editYear);
        Button addCarButton = root.findViewById(R.id.addCarButton);


        addCarButton.setOnClickListener(v -> {
            String vin = editVinField.getText().toString();
            String make = editMakeField.getText().toString();
            String model = editModel.getText().toString();
            String trim = editTrim.getText().toString();
            int year = Integer.parseInt(editYear.getText().toString());

            ArrayList userIds = new ArrayList<String>();
            userIds.add("rjdx2qXKhhZTxwZBssB0N37hrPD2");
            CarModel newCar = new CarModel(vin, make, model, trim, year, "rjdx2qXKhhZTxwZBssB0N37hrPD2", userIds);

            if(validateForm(vin, make, model, trim, year)) {
                CarController.createCarByUserId(vin, make, model, trim, year, "rjdx2qXKhhZTxwZBssB0N37hrPD2", new CarController.CarListener() {

                    @Override
                    public void onCarFetched(CarModel car) {

                    }

                    @Override
                    public void onCarsFetched(ArrayList<CarModel> cars) {

                    }

                    @Override
                    public void onCreation(String docId) {
                        CarFragment carFragment = carFragmentWeakReference.get();
                        newCar.setId(docId);
                        carFragment.addCar(newCar);

                        finish();
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
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
