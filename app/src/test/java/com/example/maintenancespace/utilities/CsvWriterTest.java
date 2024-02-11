package com.example.maintenancespace.utilities;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.content.Intent;

import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.google.firebase.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class CsvWriterTest {

    ArrayList<MaintenanceEventModel> events = new ArrayList();

    @BeforeEach
    public void beforeEach() {
        events.clear();
        for(int i = 0; i < 10; i++) {
            MaintenanceEventModel newEvent = new MaintenanceEventModel("name" + i, "desc" + i, new Timestamp(10000 * i, 0));
            events.add(newEvent);
        }
    }

    @Test
    public void shouldStartAnActivityWithTheCorrectIntent() {
        try(MockedConstruction<Intent> mockedIntent = Mockito.mockConstruction(Intent.class)) {
            try(MockedConstruction<Activity> mockedActivity = Mockito.mockConstruction(Activity.class)) {
                Activity activity = new Activity();
                doNothing().when(activity).startActivityForResult(isA(Intent.class), isA(Integer.class));
                CsvWriter.generateMaintenanceReports(activity, events);

                List<Intent> constructed =  mockedIntent.constructed();

                verify(activity, times(1)).startActivityForResult(isA(Intent.class), isA(Integer.class));
            }
        }
    }
}
