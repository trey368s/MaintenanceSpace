package com.example.maintenancespace.models.cars;

import com.example.maintenancespace.models.dailyDistance.DailyDistanceModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CarModel implements Serializable {
    private String id;
    private String vin;
    private String make;
    private String model;
    private String trim;
    private int year;
    private String ownerId;
    private ArrayList<String> userIds;
    private ArrayList<DailyDistanceModel> dailyDistanceDays;

    public CarModel(){

    }

    public CarModel(String id, String vin, String make, String model, String trim, int year, String ownerId, ArrayList<String> userIds, ArrayList<DailyDistanceModel> dailyDistanceDays){
        this.id = id;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.year = year;
        this.ownerId = ownerId;
        this.userIds = userIds;
        this.dailyDistanceDays = dailyDistanceDays;
    }

    public CarModel(String vin, String make, String model, String trim, int year, String ownerId, ArrayList<String> userIds){
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.year = year;
        this.ownerId = ownerId;
        this.userIds = userIds;
    }

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public String getVin(){
        return vin;
    }

    public void setMake(String make) { this.make = make; }

    public String getMake(){
        return make;
    }

    public void setModel(String model) { this.model = model; }

    public String getModel(){
        return model;
    }

    public void setTrim(String trim) { this.trim = trim; }

    public String getTrim(){
        return trim;
    }

    public void setYear(int year) { this.year = year; }

    public int getYear(){
        return year;
    }

    public String getOwnerId(){
        return ownerId;
    }

    public ArrayList<String> getUserIds(){
        return userIds;
    }

    public ArrayList<DailyDistanceModel> getDailyDistanceDays() { return this.dailyDistanceDays; }

    public void setDailyDistanceDays(ArrayList<DailyDistanceModel> dailyDistanceDays) { this.dailyDistanceDays = dailyDistanceDays; }

    @Override
    public String toString() {
        return this.getYear() + " " + this.getMake() + " " + this.getModel() + " " + this.getTrim();
    }
}