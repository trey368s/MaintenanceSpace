package com.example.maintenancespace.models.events;

import com.google.firebase.Timestamp;

public class MaintenanceEventModel {
    private String id;
    private String carId;
    private String name;
    private String description;
    private float mileage;
    private Timestamp date;
    private MaintenanceEventStatus status;
    private int receiptId;

    public MaintenanceEventModel() {}

    public MaintenanceEventModel(String id, String carId, String name, String description, float mileage, Timestamp date, MaintenanceEventStatus status, int receiptId) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
        this.receiptId = receiptId;
    }

    public MaintenanceEventModel(String id, String carId, String name, String description, float mileage, Timestamp date, MaintenanceEventStatus status) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
    }

    public MaintenanceEventModel(String carId, String name, String description, float mileage, Timestamp date, MaintenanceEventStatus status) {
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
    }

    public MaintenanceEventModel(String name, String description, Timestamp date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getCarId() {
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

    public Timestamp getDate() {
        return date;
    }

    public MaintenanceEventStatus getStatus() {
        return status;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
