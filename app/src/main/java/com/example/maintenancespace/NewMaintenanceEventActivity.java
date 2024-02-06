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

import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.controllers.cars.CarController;
import com.example.maintenancespace.controllers.events.MaintenanceEventController;
import com.example.maintenancespace.databinding.ActivityNewMaintenanceEventBinding;
import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.ui.events.EventFragment;
import com.example.maintenancespace.utilities.TimeHelpers;
import com.google.firebase.Timestamp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class NewMaintenanceEventActivity extends AppCompatActivity {
    public static WeakReference<EventFragment> eventFragmentWeakReference;
    private ActivityNewMaintenanceEventBinding binding;
    private ArrayList<CarModel> carList;


    public static void updateEventFragment(EventFragment fragment) {
        eventFragmentWeakReference = new WeakReference(fragment);
    }

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
                    newEvent.setId(car.getId());
                    if(validateForm(car.getId(), name, description, dateTimeStamp)) {
                        MaintenanceEventController.createByCarId(car.getId(), newEvent, new MaintenanceEventController.MaintenanceEventListener() {
                            @Override
                            public void onEventFetched(MaintenanceEventModel event) {

                            }

                            @Override
                            public void onEventsFetched(ArrayList<MaintenanceEventModel> events) {

                            }

                            @Override
                            public void onCreation(String docId) {
                                EventFragment eventFragment = eventFragmentWeakReference.get();
                                eventFragment.addEvent(newEvent);

                                finish();
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
