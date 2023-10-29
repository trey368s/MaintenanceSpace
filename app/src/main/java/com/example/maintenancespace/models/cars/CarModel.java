package com.example.maintenancespace.models.cars;

public class CarModel {
    private int id;
    private String vin;
    private String make;
    private String model;
    private String trim;
    private int year;

    public CarModel(int id, String vin, String make, String model, String trim, int year){
        this.id = id;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.trim = trim;
        this.year = year;
    }

    public int getId(){
        return id;
    }

    public String getVin(){
        return vin;
    }

    public String getMake(){
        return make;
    }

    public String getModel(){
        return model;
    }

    public String getTrim(){
        return trim;
    }

    public int getYear(){
        return year;
    }
}