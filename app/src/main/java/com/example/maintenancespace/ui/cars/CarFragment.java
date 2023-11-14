package com.example.maintenancespace.ui.cars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.databinding.FragmentCarBinding;

public class CarFragment extends Fragment {

    private FragmentCarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarViewModel carViewModel =
                new ViewModelProvider(this).get(CarViewModel.class);

        binding = FragmentCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCar;
        carViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}