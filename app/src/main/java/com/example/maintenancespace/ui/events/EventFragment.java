package com.example.maintenancespace.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.maintenancespace.NewMaintenanceEventActivity;
import com.example.maintenancespace.R;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.databinding.FragmentEventBinding;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    private FragmentEventBinding binding;

    public void addEvent(MaintenanceEventModel event) {
        final FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.eventList);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MaintenanceEventFragment newEventListItem = MaintenanceEventFragment.newInstance(event);
        fragmentTransaction.add(R.id.eventList, newEventListItem);
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        NewMaintenanceEventActivity.updateEventFragment(this);
        binding = FragmentEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final FragmentManager fragmentManager = getChildFragmentManager();
        FloatingActionButton addEventButton = root.findViewById(R.id.addMaintenanceEvent);
        addEventButton.setOnClickListener(v -> {
            Intent switchActivity = new Intent(getActivity(), NewMaintenanceEventActivity.class);
            startActivity(switchActivity);
        });

        if(savedInstanceState == null) {
            MaintenanceEventController.fetchAllByUserId("rjdx2qXKhhZTxwZBssB0N37hrPD2", new MaintenanceEventController.MaintenanceEventListener() {
                @Override
                public void onEventFetched(MaintenanceEventModel event) {

                }

                @Override
                public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    for (MaintenanceEventModel event : events) {
                        MaintenanceEventFragment eventFragment = MaintenanceEventFragment.newInstance(event);
                        fragmentTransaction.add(R.id.eventList, eventFragment);
                    }

                    fragmentTransaction.commit();
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
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}