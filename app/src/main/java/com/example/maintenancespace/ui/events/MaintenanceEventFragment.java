package com.example.maintenancespace.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.maintenancespace.R;
import com.example.maintenancespace.databinding.FragmentMaintenanceEventBinding;
import com.example.maintenancespace.models.events.MaintenanceEventModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintenanceEventFragment extends Fragment {

    private FragmentMaintenanceEventBinding binding;
    private static final String EVENT_NAME = "EVENT_NAME";
    private static final String EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    private static final String EVENT_DATE = "EVENT_DATE";
    private static final String EVENT_TIME = "EVENT_TIME";

    public static final MaintenanceEventFragment newInstance(MaintenanceEventModel maintenanceEvent) {
        MaintenanceEventFragment fragment = new MaintenanceEventFragment();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mma");
        Date date = maintenanceEvent.getDate().toDate();
        String dateFormatted = dateFormatter.format(date);
        String timeFormatted = timeFormatter.format(date).toLowerCase();


        Bundle bundle = new Bundle(2);
        bundle.putString(EVENT_NAME, maintenanceEvent.getName());
        bundle.putString(EVENT_DESCRIPTION, maintenanceEvent.getDescription());
        bundle.putString(EVENT_DATE, dateFormatted);
        bundle.putString(EVENT_TIME, timeFormatted);
        fragment.setArguments(bundle);
        return fragment ;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMaintenanceEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ConstraintLayout eventItem = binding.maintenanceEventListItem;

        TextView eventName = eventItem.findViewById(R.id.maintenanceEventName);
        TextView eventDescription = eventItem.findViewById(R.id.maintenanceEventDescription);
        TextView eventDate = eventItem.findViewById(R.id.maintenanceEventDate);
        TextView eventTime = eventItem.findViewById(R.id.maintenanceEventTime);

        eventName.setText(getArguments().getString(EVENT_NAME));
        eventDescription.setText(getArguments().getString(EVENT_DESCRIPTION));
        eventDate.setText(getArguments().getString(EVENT_DATE));
        eventTime.setText(getArguments().getString(EVENT_TIME));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}