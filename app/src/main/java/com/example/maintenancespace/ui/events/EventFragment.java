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
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.NewMaintenanceEventActivity;
import com.example.maintenancespace.R;
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.databinding.FragmentEventBinding;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventFragment extends Fragment {

    public static Fragment viewModelOwner;

    private FragmentEventBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModelOwner = this; // Create a static reference to this fragment so it can be accessed in the other files
        binding = FragmentEventBinding.inflate(inflater, container, false);
        EventViewModel viewModel = new ViewModelProvider(this).get(EventViewModel.class); // Create a view model with this fragment as the owner.
        View root = binding.getRoot();
        final FragmentManager fragmentManager = getChildFragmentManager();
        FloatingActionButton addEventButton = root.findViewById(R.id.addMaintenanceEvent);
        addEventButton.setOnClickListener(v -> {
            Intent switchActivity = new Intent(getActivity(), NewMaintenanceEventActivity.class);
            startActivity(switchActivity);
        });

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> { // observe when the events get updated
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Start a fragment transaction

            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){ // Remove all the fragments in the view
                fragmentManager.popBackStack();
            }

            if(events != null) {
                events.forEach(event -> { // For each event in the view model add it to the eventList view
                    MaintenanceEventFragment newEventListItem = MaintenanceEventFragment.newInstance(event);
                    fragmentTransaction.add(R.id.eventList, newEventListItem);
                });
            }

            fragmentTransaction.commit(); // Commit the changes.
        });

        if(savedInstanceState == null) {
            MaintenanceEventController.fetchAllByUserId(UserController.getCurrentUser().getUid(), new MaintenanceEventController.MaintenanceEventListener() {
                @Override
                public void onEventFetched(MaintenanceEventModel event) {

                }

                @Override
                public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {
                    viewModel.setEvents(events); // Set the events from the initial fetch
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