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
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.databinding.ActivityCarEditBinding;
import com.example.maintenancespace.databinding.ActivityNewCarBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.ui.cars.CarFragment;
import com.example.maintenancespace.ui.cars.CarViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CarEditActivity extends AppCompatActivity {

    public static WeakReference<CarFragment> carFragmentWeakReference;
    private ActivityCarEditBinding binding;

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

        binding = ActivityCarEditBinding.inflate(getLayoutInflater());
        CarViewModel carsViewModel = new ViewModelProvider(CarFragment.viewModelOwner).get(CarViewModel.class);
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
            userIds.add(UserController.getCurrentUser().getUid());
            CarModel newCar = new CarModel(vin, make, model, trim, year, UserController.getCurrentUser().getUid(), userIds);

            if(validateForm(vin, make, model, trim, year)) {
                CarController.createCarByUserId(vin, make, model, trim, year, UserController.getCurrentUser().getUid(), new CarController.CarListener() {

                    @Override
                    public void onCarFetched(CarModel car) {

                    }

                    @Override
                    public void onCarsFetched(ArrayList<CarModel> cars) {

                    }

                    @Override
                    public void onCreation(String docId) {
//                        ArrayList<CarModel> cars = carsViewModel.getCars().getValue();
//                        newCar.setId(docId);
//                        cars.add(newCar);
//                        carsViewModel.setCars(cars);
//
//                        finish();
                    }

                    @Override
                    public void onDelete(String docId) {

                    }

                    @Override
                    public void onUpdate(String docId) {
                        carsViewModel.setCars();
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
