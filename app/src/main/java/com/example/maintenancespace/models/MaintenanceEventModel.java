package com.example.maintenancespace.models;

import java.time.LocalDateTime;

public class MaintenanceEventModel {
    private int id;
    private int carId;
    private String name;
    private String description;
    private float mileage;
    private LocalDateTime date;
    private MaintenanceEventStatus status;
    private int receiptId;

    public MaintenanceEventModel(int id, int carId, String name, String description, float mileage, LocalDateTime date, MaintenanceEventStatus status, int receiptId) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
        this.receiptId = receiptId;
    }

    public MaintenanceEventModel(int id, int carId, String name, String description, float mileage, LocalDateTime date, MaintenanceEventStatus status) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public int getCarId() {
        return carId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getMileage() {
        return mileage;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public MaintenanceEventStatus getStatus() {
        return status;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
