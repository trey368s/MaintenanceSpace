package com.example.maintenancespace.models.events;

import com.google.firebase.Timestamp;

public class MaintenanceEventModel {
    private String id;
    private String carId;
    private String description;
    private String name;
    private float mileage;
    private Timestamp date;
    private MaintenanceEventStatus status;
    private MaintenanceEventType eventType;
    private int receiptId;

    public MaintenanceEventModel() {}

    public MaintenanceEventModel(String id, String carId, String description, float mileage, Timestamp date, MaintenanceEventStatus status, int receiptId, MaintenanceEventType eventType, String name) {
        this.id = id;
        this.carId = carId;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
        this.receiptId = receiptId;
        this.eventType = eventType;
        this.name = name;
    }

    public MaintenanceEventModel(String id, String carId, String description, float mileage, Timestamp date, MaintenanceEventStatus status, MaintenanceEventType eventType, String name) {
        this.id = id;
        this.eventType = eventType;
        this.carId = carId;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
        this.name = name;
    }

    public MaintenanceEventModel(String carId, String description, float mileage, Timestamp date, MaintenanceEventStatus status, MaintenanceEventType eventType, String name) {
        this.carId = carId;
        this.eventType = eventType;
        this.description = description;
        this.mileage = mileage;
        this.date = date;
        this.status = status;
        this.name = name;
    }

    public MaintenanceEventModel(String description, Timestamp date, MaintenanceEventType eventType, String name) {
        this.eventType = eventType;
        this.description = description;
        this.date = date;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public MaintenanceEventType getType() {
        return eventType;
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

    public String getCsvRow() {
        String nameCell = String.valueOf(this.getType());
        String descriptionCell = this.getDescription() != null && !this.getDescription().trim().isEmpty() ? this.getDescription() : "";
        String mileageCell = this.getMileage() != 0.0f ? Float.toString(this.getMileage()) : "";
        String dateCell = this.getDate() != null ? this.getDate().toDate().toString() : "";
        String statusCell = this.getStatus() != null ? this.getStatus().name() : "";

        String row = nameCell;
        row += "," + descriptionCell;
        row += "," + mileageCell;
        row += "," + dateCell;
        row += "," + statusCell;
        return row;
    }

    public static String getCsvHeaders() {
        return "Event Name,Event Description,Mileage Due At,Date Due At,Status";
    }

    public void setName(String toString) {
        this.name = toString;
    }
}
