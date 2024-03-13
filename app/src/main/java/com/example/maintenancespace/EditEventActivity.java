package com.example.maintenancespace;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.databinding.ActivityEditCarBinding;
import com.example.maintenancespace.databinding.ActivityEditEventBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.cars.CarListItemFragment;
import com.example.maintenancespace.ui.cars.CarViewModel;
import com.example.maintenancespace.ui.events.EventViewModel;
import com.example.maintenancespace.utilities.TimeHelpers;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EditEventActivity extends AppCompatActivity {
    private ActivityEditEventBinding binding;

    private boolean validateForm(String carId, String name, String description, Timestamp date) {
        if(carId.isEmpty()) {
            return false;
        }
        if(name.isEmpty()) {
            return false;
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MaintenanceEventModel existingEvent = getIntent().getExtras().getParcelable("EVENT", MaintenanceEventModel.class);

        binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        EventViewModel eventViewModel  = new ViewModelProvider(MainActivity.viewModelOwner).get(EventViewModel.class);
        CarViewModel carViewModel  = new ViewModelProvider(MainActivity.viewModelOwner).get(CarViewModel.class);
        ConstraintLayout root = binding.getRoot();
        setContentView(root);
        AutoCompleteTextView editVinField = root.findViewById(R.id.editVin);
        EditText editNameField = root.findViewById(R.id.editMake);
        EditText editDescription = root.findViewById(R.id.editModel);
        CalendarView editDateField = root.findViewById(R.id.editDate);
        TimePicker editTimeField = root.findViewById(R.id.editTime);
        Button saveEventButton = binding.saveEventButton;

        ArrayList<CarModel> cars = carViewModel.getCars().getValue();
        String carVin = "";
        for (CarModel car: cars) {
            if (car.getId().equals(existingEvent.getCarId())) {
                carVin = car.getVin();
            }
        }
        editVinField.setText(carVin);
        editNameField.setText(existingEvent.getName());
        editDescription.setText(existingEvent.getDescription());
        editDateField.setDate(existingEvent.getDate().toDate().getTime());
        SimpleDateFormat HH = new SimpleDateFormat("HH");
        SimpleDateFormat mm = new SimpleDateFormat("mm");
        String hhString = HH.format(existingEvent.getDate().toDate());
        String mmString = mm.format(existingEvent.getDate().toDate());
        int hhInt = Integer.parseInt(hhString);
        int mmInt = Integer.parseInt(mmString);
        Log.d("hh", existingEvent.getDate().toDate().toString());
        editTimeField.setHour(hhInt);
        editTimeField.setMinute(mmInt);

        Button deleteEventButton = binding.deleteEventButton;
        deleteEventButton.setOnClickListener(v -> {
            MaintenanceEventController.deleteById(existingEvent.getCarId(), existingEvent.getId(), new MaintenanceEventController.MaintenanceEventListener() {
                @Override
                public void onEventFetched(MaintenanceEventModel event) {

                }

                @Override
                public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {

                }

                @Override
                public void onCreation(String docId) {

                }

                @Override
                public void onDelete(String docId) {
                    ArrayList<MaintenanceEventModel> events = eventViewModel.getEvents().getValue();
                    for(int i = 0; i < events.size(); i++) {
                        if(events.get(i).getId().equals(existingEvent.getId())) {
                            events.remove(i);
                        }
                    }

                    eventViewModel.setEvents(events);
                    finish();
                }

                @Override
                public void onUpdate(String docId) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });

        saveEventButton.setOnClickListener(v -> {
            String vin = editVinField.getText().toString();
            String name = editNameField.getText().toString();
            String description = editDescription.getText().toString();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(editDateField.getDate()));
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.HOUR, editTimeField.getHour());
            cal.set(Calendar.MINUTE, editTimeField.getMinute());
            long secondsSinceEpoch = TimeHelpers.convertMillisecondsToSeconds(cal.getTimeInMillis());
            Timestamp dateTimeStamp = new Timestamp(secondsSinceEpoch, 0);

            ArrayList userIds = new ArrayList<String>();
            userIds.add(UserController.getCurrentUser().getUid());
            existingEvent.setName(name);
            existingEvent.setDescription(description);
            existingEvent.setDate(dateTimeStamp);

            ArrayList<CarModel> carList = carViewModel.getCars().getValue();
            for(CarModel car : carList) {
                if(Objects.equals(car.getVin(), vin.toString())) {
                    existingEvent.setCarId(car.getId());
                    if(validateForm(car.getVin(), name, description, dateTimeStamp)) {
                        MaintenanceEventController.updateById(car.getId(), existingEvent.getId(), existingEvent, new MaintenanceEventController.MaintenanceEventListener() {
                            @Override
                            public void onEventFetched(MaintenanceEventModel event) {

                            }

                            @Override
                            public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {

                            }

                            @Override
                            public void onCreation(String docId) {

                            }

                            @Override
                            public void onDelete(String docId) {

                            }

                            @Override
                            public void onUpdate(String docId) {
                                ArrayList<MaintenanceEventModel> events = eventViewModel.getEvents().getValue();

                                for (int i = 0; i < events.size(); i++) {
                                    if(events.get(i).getId().equals(existingEvent.getId())) {
                                        events.set(i, existingEvent);
                                    }
                                }

                                eventViewModel.setEvents(events);

                                finish(); // Call finish to end the activity
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                    break;
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
