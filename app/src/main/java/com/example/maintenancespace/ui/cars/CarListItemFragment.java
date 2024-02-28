package com.example.maintenancespace.ui.cars;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.maintenancespace.EditCarActivity;
import com.example.maintenancespace.R;
import com.example.maintenancespace.databinding.FragmentCarListItemBinding;
import com.example.maintenancespace.databinding.FragmentMaintenanceEventBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CarListItemFragment extends Fragment {

    public static final String CAR_ID_KEY = "CAR_ID_KEY";
    private FragmentCarListItemBinding binding;
    private static final String CAR_NAME = "CAR_NAME";

    public static final CarListItemFragment newInstance(CarModel car) {
        CarListItemFragment fragment = new CarListItemFragment();

        Bundle bundle = new Bundle(1);
        bundle.putString(CAR_NAME, car.getYear() + " " + car.getTrim() + " " + car.getMake() + " " + car.getModel());
        bundle.putString(CAR_ID_KEY, car.getId());
        bundle.putSerializable("CAR", car);
        fragment.setArguments(bundle);
        return fragment ;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarListItemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Intent editIntent = new Intent(getActivity(), EditCarActivity.class);
        editIntent.putExtra(CAR_ID_KEY, getArguments().getString(CAR_ID_KEY));
        editIntent.putExtra("CAR", getArguments().getSerializable("CAR", CarModel.class));

        final ConstraintLayout carItem = binding.CarListItem;
        carItem.setOnClickListener(e -> {
            startActivity(editIntent);
        });

        TextView eventName = carItem.findViewById(R.id.carName);

        eventName.setText(getArguments().getString(CAR_NAME));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}