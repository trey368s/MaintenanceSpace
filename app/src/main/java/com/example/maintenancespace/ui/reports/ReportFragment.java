package com.example.maintenancespace.ui.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.maintenancespace.R;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.databinding.FragmentReportBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.cars.CarSpinner;
import com.example.maintenancespace.utilities.CsvWriter;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button generateReportButton = root.findViewById(R.id.buttonGenerateReport);
        CarSpinner carSpinner = root.findViewById(R.id.carSpinner);

        generateReportButton.setOnClickListener(v -> {
            CarModel selectedCar = (CarModel) carSpinner.getSelectedItem();
            Log.d("Car ID", selectedCar.getId());
            MaintenanceEventController.fetchAllByCarId(selectedCar.getId(), new MaintenanceEventController.MaintenanceEventListener() {
                @Override
                public void onEventFetched(MaintenanceEventModel event) {

                }

                @Override
                public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {
                    CsvWriter.generateMaintenanceReports(getActivity(), events);
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
            }, false);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}