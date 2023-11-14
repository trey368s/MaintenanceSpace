package com.example.maintenancespace.ui.cars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.databinding.FragmentCarBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;
    private CarController carController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarViewModel carViewModel =
                new ViewModelProvider(this).get(CarViewModel.class);

        binding = FragmentCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCar;
        carController = new CarController(FirebaseFirestore.getInstance());

        carController.fetchCarById("QT990zBSHVLNs9OPDdSx", new CarController.CarListener() {
        //carController.fetchAllCarsByUserId("rjdx2qXKhhZTxwZBssB0N37hrPD2", new CarController.CarListener() {
        //carController.createCarByUserId("ABCDE12345", "Toyota", "Corolla", "Blue", 2015, "XYZ987", new CarController.CarListener() {
        //carController.deleteCarById("tR0XUDLcI4vnUVPNpY8s", new CarController.CarListener() {
        /*ArrayList<String> userIds = new ArrayList<>();
        userIds.add("XYZ987");
        userIds.add("rjdx2qXKhhZTxwZBssB0N37hrPD2");
        CarModel updatedCar = new CarModel("ABCDE12345", "Toyota", "Corolla", "Blue", 2015, "XYZ987", userIds);
        carController.updateCarById("xGekphWFBtrj9u4Eedav", updatedCar, new CarController.CarListener() {*/
            @Override
            public void onCarFetched(CarModel car) {
                textView.setText("Car Details: " + car.getYear() + " " + car.getTrim() + " " + car.getMake() + " " + car.getModel());
            }

            @Override
            public void onCarsFetched(ArrayList<CarModel> cars) {
                String resultView = "";
                for (CarModel car : cars) {
                    resultView = resultView + car.getMake() + ", ";
                }
                textView.setText("Makes of Cars: " + resultView);
            }

            @Override
            public void onCreation(String docId){
                textView.setText("Created Car ID: " + docId);
            }

            @Override
            public void onDelete(String docId){
                textView.setText("Deleted Car ID: " + docId);
            }

            @Override
            public void onUpdate(String docId){
                textView.setText("Updated Car ID: " + docId);
            }

            @Override
            public void onFailure(Exception e) {
                textView.setText("Error: " + e.getMessage());
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