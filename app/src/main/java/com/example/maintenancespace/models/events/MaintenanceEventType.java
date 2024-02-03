package com.example.maintenancespace.models.events;

public enum MaintenanceEventType {
    OIL_CHANGE(1, "Oil Change"),
    TIRE_ROTATION(2, "Tire Rotation"),
    BRAKE_INSPECTION(3, "Brake Inspection"),
    OTHER(4,"Other");

    private final int intValue;
    private final String displayName;

    MaintenanceEventType(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    public int getIntValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
