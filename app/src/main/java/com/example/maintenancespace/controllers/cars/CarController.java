package com.example.maintenancespace.controllers.cars;

import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.users.UserModel;

import java.util.ArrayList;

public class CarController {
    public static CarModel fetchById(int carId){
        return new CarModel(carId, "VIN12345", "Tesla", "Model 3", "Silver", 2023, "1");
    }

    public static ArrayList<CarModel> fetchAllCarsByUser(String userId){
        CarModel car = new CarModel(1,"VIN12345", "Tesla", "Model 3", "Silver", 2023, userId);
        ArrayList<CarModel> cars = new ArrayList<>();
        cars.add(car);
        return cars;
    }

    public static boolean deleteById(int carId) {
        return true;
    }

    public static CarModel createByUserId(String vin, String make, String model, String trim, int year, String ownerId) {
        return new CarModel(1, vin, make, model, trim, year, ownerId);
    }

    public static CarModel updateById(int carId, String vin, String make, String model, String trim, int year, String ownerId) {
        return new CarModel(carId, vin, make, model, trim, year, ownerId);
    }
}
