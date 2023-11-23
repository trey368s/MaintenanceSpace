package com.example.maintenancespace.ui.cars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.R;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.databinding.FragmentCarBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.events.MaintenanceEventFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final FragmentManager fragmentManager = getChildFragmentManager();

        CarController.fetchAllCarsByUserId("rjdx2qXKhhZTxwZBssB0N37hrPD2", new CarController.CarListener() {

            @Override
            public void onCarFetched(CarModel car) {
                //textView.setText("Car Details: " + car.getYear() + " " + car.getTrim() + " " + car.getMake() + " " + car.getModel());
            }

            @Override
            public void onCarsFetched(ArrayList<CarModel> cars) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                for (CarModel car : cars) {
                    CarListItemFragment existingFragment = (CarListItemFragment) fragmentManager.findFragmentByTag(car.getVin());
                    if (existingFragment == null) {
                        CarListItemFragment eventFragment = CarListItemFragment.newInstance(car);
                        fragmentTransaction.add(R.id.car_layout, eventFragment, car.getVin());
                    }
                }

                fragmentTransaction.commit();
            }

            @Override
            public void onCreation(String docId){
                //textView.setText("Created Car ID: " + docId);
            }

            @Override
            public void onDelete(String docId){
                //textView.setText("Deleted Car ID: " + docId);
            }

            @Override
            public void onUpdate(String docId){
                //textView.setText("Updated Car ID: " + docId);
            }

            @Override
            public void onFailure(Exception e) {
                //textView.setText("Error: " + e.getMessage());
            }
        });

    return root;
}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}