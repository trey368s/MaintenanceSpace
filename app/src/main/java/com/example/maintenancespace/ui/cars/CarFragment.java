package com.example.maintenancespace.ui.cars;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.NewCarActivity;
import com.example.maintenancespace.R;
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.databinding.FragmentCarBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;

    public static Fragment viewModelOwner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModelOwner = this;
        binding = FragmentCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        CarViewModel viewModel = new ViewModelProvider(this).get(CarViewModel.class);
        final FragmentManager fragmentManager = getChildFragmentManager();
        FloatingActionButton addCarButton = root.findViewById(R.id.addCar);
        addCarButton.setOnClickListener(v -> {
            Intent switchActivity = new Intent(getActivity(), NewCarActivity.class);
            startActivity(switchActivity);
        });

        
        viewModel.getCars().observe(getViewLifecycleOwner(), cars -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
                fragmentManager.popBackStack();
            }

            if(cars != null) {
                cars.forEach(car -> {
                    CarListItemFragment existingFragment = (CarListItemFragment) fragmentManager.findFragmentByTag(car.getId());
                    if(existingFragment != null) {
                        fragmentTransaction.remove(existingFragment);
                    }
                    CarListItemFragment newCarListItem = CarListItemFragment.newInstance(car);
                    fragmentTransaction.add(R.id.car_layout, newCarListItem, car.getId());
                });
            }

            fragmentTransaction.commit();
        });

            CarController.fetchAllCarsByUserId(UserController.getCurrentUser().getUid(), new CarController.CarListener() {

                @Override
                public void onCarFetched(CarModel car) {

                }

                @Override
                public void onCarsFetched(ArrayList<CarModel> cars) {
                    viewModel.setCars(cars);
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