package com.example.maintenancespace.ui.gps;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;

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
        TextView timerText = binding.tripTimerNumber;
        TextView distanceText = binding.tripDistanceNumber;

        viewModel.getTripTimer().observe(getViewLifecycleOwner(), time -> {
            Date d = new Date(time);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeText = df.format(d);
            timerText.setText(timeText);
        });

        viewModel.getTripDistance().observe(getViewLifecycleOwner(), distance -> {
            float distanceInKM = distance / 1000;
            distanceText.setText(Float.toString(distanceInKM));
        });

        startTripButton.setOnClickListener(v -> {
            CarModel selectedCar = (CarModel) carSpinner.getSelectedItem();
            tripIntent.putExtra("CAR_ID", selectedCar.getId());

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