package com.example.maintenancespace.ui.gps;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.MainActivity;
import com.example.maintenancespace.R;
import com.example.maintenancespace.databinding.FragmentGpsBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.services.TripService;
import com.example.maintenancespace.ui.cars.CarSpinner;

public class GpsFragment extends Fragment {

    private FragmentGpsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGpsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        GpsViewModel viewModel = new ViewModelProvider(getActivity()).get(GpsViewModel.class);
        Intent tripIntent = new Intent(getActivity(), TripService.class);

        final Button startTripButton = binding.buttonStartTrip;
        CarSpinner carSpinner = root.findViewById(R.id.tripCarSpinner);

        startTripButton.setOnClickListener(v -> {
            CarModel selectedCar = (CarModel) carSpinner.getSelectedItem();
            tripIntent.putExtra("carId", selectedCar.getId());

            if(!viewModel.getIsTripServiceRunning().getValue()) {
                getActivity().startService(tripIntent);
            } else {
                getActivity().stopService(tripIntent);
            }
        });

        viewModel.getIsTripServiceRunning().observe(getViewLifecycleOwner(), isRunning -> {
            binding.buttonStartTrip.setText(isRunning ? "Stop Trip" : "Start Trip");
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}