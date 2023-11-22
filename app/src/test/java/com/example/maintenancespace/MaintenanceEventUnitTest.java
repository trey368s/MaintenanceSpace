package com.example.maintenancespace;

import static org.junit.Assert.assertEquals;

import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.google.firebase.Timestamp;

import org.junit.Test;

public class MaintenanceEventUnitTest {
    @Test
    public void shouldGetCorrectName() {
        MaintenanceEventModel event = new MaintenanceEventModel("Oil Change", "Description", new Timestamp(100,100));
        String expected = "Oil Change";
        assertEquals(event.getName(), expected);
    }
}
