package com.example.maintenancespace;

import static org.junit.Assert.assertEquals;

import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.models.events.MaintenanceEventStatus;
import com.example.maintenancespace.models.events.MaintenanceEventType;
import com.google.firebase.Timestamp;

import org.junit.Test;

public class MaintenanceEventModelUnitTest {
    @Test
    public void shouldGetCorrectName() {
        MaintenanceEventModel event = new MaintenanceEventModel("Description", new Timestamp(100,100), MaintenanceEventType.OIL_CHANGE, MaintenanceEventType.OIL_CHANGE.toString());
        String expected = "Oil Change";
        assertEquals(expected, event.getType());
    }

    @Test
    public void shouldReturnCorrectCsvHeaderString() {
        String headerString = MaintenanceEventModel.getCsvHeaders();
        String expected = "Event Name,Event Description,Mileage Due At,Date Due At,Status";
        assertEquals(expected, headerString);
    }

    @Test
    public void shouldReturnCorrectCsvRowString() {
        MaintenanceEventModel event = new MaintenanceEventModel("1", "Oil Change", "Foo Description", 203.1f, new Timestamp(1702433835, 0), MaintenanceEventStatus.FUTURE, 1, MaintenanceEventType.OIL_CHANGE, MaintenanceEventType.OIL_CHANGE.toString());
        String rowString = event.getCsvRow();
        String expected = "Oil Change,Foo Description,203.1,Tue Dec 12 21:17:15 EST 2023,FUTURE";
        assertEquals(expected, rowString);
    }
}
