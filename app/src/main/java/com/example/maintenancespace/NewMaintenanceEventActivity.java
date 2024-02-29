package com.example.maintenancespace;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.databinding.ActivityNewMaintenanceEventBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.events.EventFragment;
import com.example.maintenancespace.ui.events.EventViewModel;
import com.example.maintenancespace.utilities.TimeHelpers;
import com.google.firebase.Timestamp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class NewMaintenanceEventActivity extends AppCompatActivity {
    private ActivityNewMaintenanceEventBinding binding;
    private ArrayList<CarModel> carList;


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

        binding = ActivityNewMaintenanceEventBinding.inflate(getLayoutInflater());
        ConstraintLayout root = binding.getRoot();
        setContentView(root);
        AutoCompleteTextView editVinField = root.findViewById(R.id.editVin);
        EditText editNameField = root.findViewById(R.id.editMake);
        EditText editDescription = root.findViewById(R.id.editModel);
        CalendarView editDateField = root.findViewById(R.id.editDate);
        TimePicker editTimeField = root.findViewById(R.id.editTime);
        Button addCarButton = root.findViewById(R.id.addEventButton);

        EventViewModel eventsViewModel = new ViewModelProvider(MainActivity.viewModelOwner).get(EventViewModel.class); // Get the view model from the fragment


        addCarButton.setOnClickListener(v -> {
            String vin = editVinField.getText().toString();
            String name = editNameField.getText().toString();
            String description = editDescription.getText().toString();
            long dateTime = TimeHelpers.convertMillisecondsToSeconds(editDateField.getDate());
            long hoursInSeconds = TimeHelpers.convertHoursToSeconds(editTimeField.getHour());
            long minutesInSeconds = TimeHelpers.convertMinutesToSeconds(editTimeField.getMinute());
            long secondsSinceEpoch = dateTime + hoursInSeconds + minutesInSeconds;

            Timestamp dateTimeStamp = new Timestamp(secondsSinceEpoch, 0);

            MaintenanceEventModel newEvent = new MaintenanceEventModel(name, description, dateTimeStamp);

            for(CarModel car : carList) {
                if(Objects.equals(car.getVin(), vin.toString())) {
                    newEvent.setCarId(car.getId());
                    if(validateForm(car.getVin(), name, description, dateTimeStamp)) {
                        MaintenanceEventController.createByCarId(car.getId(), newEvent, new MaintenanceEventController.MaintenanceEventListener() {
                            @Override
                            public void onEventFetched(MaintenanceEventModel event) {

                            }

                            @Override
                            public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {

                            }

                            @Override
                            public void onCreation(String docId) {
                                ArrayList<MaintenanceEventModel> events = eventsViewModel.getEvents().getValue(); // Get the current list of events
                                newEvent.setId(docId);
                                events.add(newEvent); // Add the new event to the list
                                eventsViewModel.setEvents(events); // Set the events so the change can be observed

                                finish(); // Call finish to end the activity
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
                    break;
                }
            }
        });

        CarController.fetchAllCarsByUserId(UserController.getCurrentUser().getUid(), new CarController.CarListener() {
            @Override
            public void onCarFetched(CarModel car) {

            }

            @Override
            public void onCarsFetched(ArrayList<CarModel> cars) {
                ArrayList carVins = new ArrayList<String>();
                carList = cars;
                for(CarModel car : cars) {
                    carVins.add(car.getVin());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(
                        NewMaintenanceEventActivity.this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        carVins);
                editVinField.setAdapter(adapter);
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
        editVinField.setAutofillHints();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
