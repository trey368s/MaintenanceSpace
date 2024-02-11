package com.example.maintenancespace.models.events;

import static org.junit.Assert.assertEquals;

import com.google.firebase.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MaintenanceEventModelTest {

    MaintenanceEventModel event;

    @BeforeEach
    public void beforeEach() {
      event = new MaintenanceEventModel("1", "2", "Oil Change", "Foo Description", 203.1f, new Timestamp(1702433835, 0), MaintenanceEventStatus.FUTURE, 1);
    }
    @Test
    public void shouldBeAbleToCreateEmptyEvent() {
        assertEquals(MaintenanceEventModel.class, new MaintenanceEventModel().getClass());
    }

    @Test
    public void shouldBeAbleToCreateEventWithNameDescDate() {
        MaintenanceEventModel newEvent = new MaintenanceEventModel("name", "desc", new Timestamp(100000, 0));
        assertEquals(MaintenanceEventModel.class, newEvent.getClass());
    }

    @Test
    public void shouldSetCorrectId() {
        String expected = "newId";

        event.setId(expected);

        assertEquals(expected, event.getId());
    }


    @Test
    public void shouldGetCorrectId() {
        String expected = "1";
        assertEquals(expected, event.getId());
    }

    @Test
    public void shouldGetCorrectCarId() {
        String expected = "2";
        assertEquals(expected, event.getCarId());
    }

    @Test
    public void shouldGetCorrectName() {
        String expected = "Oil Change";
        assertEquals(expected, event.getName());
    }

    @Test
    public void shouldGetCorrectDescription() {
        String expected = "Foo Description";
        assertEquals(expected, event.getDescription());
    }

    @Test
    public void shouldGetCorrectMileage() {
        float expected = 203.1f;
        assertEquals(expected, event.getMileage(), 0f);
    }

    @Test
    public void shouldGetCorrectDate() {
        Timestamp expected = new Timestamp(1702433835, 0);
        assertEquals(expected, event.getDate());
    }

    @Test
    public void shouldGetStatus() {
        MaintenanceEventStatus expected = MaintenanceEventStatus.FUTURE;
        assertEquals(expected, event.getStatus());
    }

    @Test
    public void shouldGetReceiptId() {
        int expected = 1;
        assertEquals(expected, event.getReceiptId());
    }

    @Test
    public void shouldReturnCorrectCsvHeaderString() {
        String headerString = MaintenanceEventModel.getCsvHeaders();
        String expected = "Event Name,Event Description,Mileage Due At,Date Due At,Status";
        assertEquals(expected, headerString);
    }

    @Test
    public void shouldReturnCorrectCsvRowString() {
        String rowString = event.getCsvRow();
        String expected = "Oil Change,Foo Description,203.1,Tue Dec 12 21:17:15 EST 2023,FUTURE";
        assertEquals(expected, rowString);
    }
}
