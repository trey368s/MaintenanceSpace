package com.example.maintenancespace.models.cars;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;

public class CarModelTest {

    CarModel car;

    @BeforeEach
    public void beforeEach() {
        ArrayList<String> userIds = new ArrayList(Arrays.asList("1", "2", "3"));
        car = new CarModel("carId", "vin", "make", "model", "trim", 2020, "ownerId", userIds);
    }

    @Test
    public void shouldCreateAnEmptyCar() {
        assertEquals(CarModel.class, new CarModel().getClass());
    }

    @Test
    public void shouldBeAbleToCreateCarWithNoId() {
        ArrayList<String> userIds = new ArrayList(Arrays.asList("1", "2", "3"));
        CarModel newCar = new CarModel("vin", "make",  "model", "trim", 1999, "ownerId", userIds);

        assertEquals(null, newCar.getId());
    }

    @Test
    public void shouldSetCorrectId() {
        String expected = "newCarId";

        car.setId("newCarId");

        assertEquals(expected, car.getId());
    }

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
