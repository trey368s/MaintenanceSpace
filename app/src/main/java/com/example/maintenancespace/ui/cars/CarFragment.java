package com.example.maintenancespace.ui.cars;

import android.content.Intent;
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

import com.example.maintenancespace.NewCarActivity;
import com.example.maintenancespace.R;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.databinding.FragmentCarBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.events.MaintenanceEventFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;

    public void addCar(CarModel car) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.car_layout);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CarListItemFragment existingFragment = (CarListItemFragment) fragmentManager.findFragmentByTag(car.getId());
        if (existingFragment == null) {
            CarListItemFragment newEventListItem = CarListItemFragment.newInstance(car);
            fragmentTransaction.add(R.id.car_layout, newEventListItem, car.getId());
        }
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        NewCarActivity.updateCarFragment(this);
        binding = FragmentCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final FragmentManager fragmentManager = getChildFragmentManager();
        FloatingActionButton addCarButton = root.findViewById(R.id.addCar);
        addCarButton.setOnClickListener(v -> {
            Intent switchActivity = new Intent(getActivity(), NewCarActivity.class);
            startActivity(switchActivity);
        });

        CarController.fetchAllCarsByUserId("rjdx2qXKhhZTxwZBssB0N37hrPD2", new CarController.CarListener() {

            @Override
            public void onCarFetched(CarModel car) {
                //textView.setText("Car Details: " + car.getYear() + " " + car.getTrim() + " " + car.getMake() + " " + car.getModel());
            }

            @Override
            public void onCarsFetched(ArrayList<CarModel> cars) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                for (CarModel car : cars) {
                    CarListItemFragment existingFragment = (CarListItemFragment) fragmentManager.findFragmentByTag(car.getId());
                    if (existingFragment == null) {
                        CarListItemFragment eventFragment = CarListItemFragment.newInstance(car);
                        fragmentTransaction.add(R.id.car_layout, eventFragment, car.getId());
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