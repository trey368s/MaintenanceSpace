package com.example.maintenancespace.models.cars;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CarModelTest {

    ArrayList<String> userIds = new ArrayList(Arrays.asList("1", "2", "3"));
    CarModel car = new CarModel("carId", "vin", "make", "model", "trim", 2020, "ownerId", userIds);

    @Test
    public void shouldGetCorrectId() {
        String expected = "carId";
        assertEquals(expected, car.getId());
    }

    @Test
    public void shouldGetCorrectVin() {
        String expected = "vin";
        assertEquals(expected, car.getVin());
    }

    @Test
    public void shouldGetCorrectMake() {
        String expected = "make";
        assertEquals(expected, car.getMake());
    }

    @Test
    public void shouldGetCorrectModel() {
        String expected = "model";
        assertEquals(expected, car.getModel());
    }

    @Test
    public void shouldGetCorrectTrim() {
        String expected = "trim";
        assertEquals(expected, car.getTrim());
    }

    @Test
    public void shouldGetCorrectYear() {
        int expected = 2020;
        assertEquals(expected, car.getYear());
    }

    @Test
    public void shouldGetCorrectOwnerId() {
        String expected = "ownerId";
        assertEquals(expected, car.getOwnerId());
    }

    @Test
    public void shouldGetCorrectUserIds() {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));
        assertEquals(expected, car.getUserIds());
    }


    @Test
    public void shouldConvertToCorrectString() {
        String carString = car.toString();
        String expected = "2020 make model trim";
        assertEquals(expected, carString);
    }
}
