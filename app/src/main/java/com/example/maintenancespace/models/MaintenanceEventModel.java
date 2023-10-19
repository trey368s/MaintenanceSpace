package com.example.maintenancespace.models;

import java.time.LocalDateTime;

public class MaintenanceEventModel {
    private int id;
    private int cardId;
    private String name;
    private String description;
    private float mileage;
    private LocalDateTime date;
    private MaintenanceEventStatus status;
    private int receipt;

    public MaintenanceEventModel(int id, int carId, String name, String description, float mileage, LocalDateTime date, MaintenanceEventStatus status, int receipt) {
        this.id = id;
        this.cardId = carId;
        this.name = name;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
        this.receipt = receipt;
    }
}
