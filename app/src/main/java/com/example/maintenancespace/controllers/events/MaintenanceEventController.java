package com.example.maintenancespace.controllers.events;

import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.example.maintenancespace.models.events.MaintenanceEventStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MaintenanceEventController {
    public static MaintenanceEventModel fetchById(int maintenanceEventId) {
        return new MaintenanceEventModel(maintenanceEventId, 2, "Oil Change", "Change oil", 0f, LocalDateTime.now(), MaintenanceEventStatus.FUTURE, 0);
    }

    public static ArrayList<MaintenanceEventModel> fetchAllByCarId(int carId) {
        MaintenanceEventModel event = new MaintenanceEventModel(1, carId, "Oil Change", "Change oil", 0f, LocalDateTime.now(), MaintenanceEventStatus.FUTURE, 0);
        ArrayList<MaintenanceEventModel> events = new ArrayList<>();
        events.add(event);
        return events;
    }

    public static boolean deleteById(int maintenanceEventId) {
        return true;
    }

    public static MaintenanceEventModel createByCarId(int carId, String name, String description, Float mileage, LocalDateTime date, MaintenanceEventStatus status) {
        return new MaintenanceEventModel(1, carId, name, description, mileage, date, status);
    }

    public static MaintenanceEventModel updateById(int maintenanceEventId, int carId, String name, String description, Float mileage, LocalDateTime date, MaintenanceEventStatus status) {
        return new MaintenanceEventModel(maintenanceEventId, carId, name, description, mileage, date, status);
    }

    public static boolean deleteAllByCarId(int carId) {
        return true;
    }
}
