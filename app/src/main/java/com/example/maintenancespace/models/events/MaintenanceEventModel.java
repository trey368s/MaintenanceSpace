package com.example.maintenancespace.models.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class MaintenanceEventModel implements Parcelable {
    private String id;
    private String carId;
    private String name;
    private String description;
    private float distance;
    private Timestamp date;
    private MaintenanceEventStatus status;
    private int receiptId;

    public MaintenanceEventModel() {}

    public MaintenanceEventModel(String id, String carId, String name, String description, float distance, Timestamp date, MaintenanceEventStatus status, int receiptId) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.distance = distance;
        this.date = date;
        this.status = status;
        this.receiptId = receiptId;
    }

    public MaintenanceEventModel(String id, String carId, String name, String description, float distance, Timestamp date, MaintenanceEventStatus status) {
        this.id = id;
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.distance = distance;
        this.date = date;
        this.status = status;
    }

    public MaintenanceEventModel(String carId, String name, String description, float distance, Timestamp date, MaintenanceEventStatus status) {
        this.carId = carId;
        this.name = name;
        this.description = description;
        this.distance = distance;
        this.date = date;
        this.status = status;
    }

    public MaintenanceEventModel(String name, String description, Timestamp date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    protected MaintenanceEventModel(Parcel in) {
        id = in.readString();
        carId = in.readString();
        name = in.readString();
        description = in.readString();
        distance = in.readFloat();
        date = in.readParcelable(Timestamp.class.getClassLoader());
        receiptId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(carId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(distance);
        dest.writeParcelable(date, flags);
        dest.writeInt(receiptId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MaintenanceEventModel> CREATOR = new Creator<MaintenanceEventModel>() {
        @Override
        public MaintenanceEventModel createFromParcel(Parcel in) {
            return new MaintenanceEventModel(in);
        }

        @Override
        public MaintenanceEventModel[] newArray(int size) {
            return new MaintenanceEventModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public void setCarId(String id) { this.carId = id; }
    public String getCarId() {
        return carId;
    }

    public void setName(String name) {this.name = name;}
    public String getName() {
        return name;
    }

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {
        return description;
    }

    public float getDistance() {
        return distance;
    }

    public void setDate(Timestamp date) { this.date = date; }
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
        String nameCell = this.getName();
        String descriptionCell = this.getDescription() != null && !this.getDescription().trim().isEmpty() ? this.getDescription() : "";
        String distanceCell = this.getDistance() != 0.0f ? Float.toString(this.getDistance()) : "";
        String dateCell = this.getDate() != null ? this.getDate().toDate().toString() : "";
        String statusCell = this.getStatus() != null ? this.getStatus().name() : "";

        String row = nameCell;
        row += "," + descriptionCell;
        row += "," + distanceCell;
        row += "," + dateCell;
        row += "," + statusCell;
        return row;
    }

    public static String getCsvHeaders() {
        return "Event Name,Event Description,Due At Meters,Date Due At,Status";
    }
}
