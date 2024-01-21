package com.example.maintenancespace.controllers.cars;

import com.example.maintenancespace.models.cars.CarModel;
import com.example.maintenancespace.models.dailyDistance.DailyDistanceModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CarController {

    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public interface CarListener {
        void onCarFetched(CarModel car);
        void onCarsFetched(ArrayList<CarModel> cars);
        void onDailyDistanceUpdate(String carId);
        void onCreation(String docId);
        void onDelete(String docId);
        void onUpdate(String docId);
        void onFailure(Exception e);
    }

    public static void fetchCarById(String carId, CarListener listener){
        firestore.collection("Car").document(carId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        CarModel car = documentSnapshot.toObject(CarModel.class);
                        listener.onCarFetched(car);
                    } else {
                        listener.onFailure(new Exception("Car not found"));
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void fetchAllCarsByUserId(String userId, CarListener listener) {
        firestore.collection("Car")
                .whereArrayContains("userIds", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<CarModel> cars = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        CarModel car = document.toObject(CarModel.class);
                        car.setId(document.getId());
                        cars.add(car);
                    }
                    listener.onCarsFetched(cars);
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void createCarByUserId(String vin, String make, String model, String trim, int year, String ownerId, CarListener listener) {
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(ownerId);
        CarModel car = new CarModel(vin, make, model, trim, year, ownerId, userIds);
        firestore.collection("Car")
                .add(car)
                .addOnSuccessListener(documentReference -> {
                    String docId = documentReference.getId();
                    listener.onCreation(docId);
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }


    public static void deleteCarById(String carId, CarListener listener) {
        firestore.collection("Car")
                .document(carId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onDelete(carId);
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                });
    }

    public static void updateCarById(String carId, CarModel updatedCar, CarListener listener) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("vin", updatedCar.getVin());
        updatedData.put("make", updatedCar.getMake());
        updatedData.put("model", updatedCar.getModel());
        updatedData.put("trim", updatedCar.getTrim());
        updatedData.put("year", updatedCar.getYear());
        updatedData.put("ownerId", updatedCar.getOwnerId());
        updatedData.put("userIds", updatedCar.getUserIds());

        firestore.collection("Car")
                .document(carId)
                .set(updatedData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    listener.onUpdate(carId);
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                });
    }

    public static void updateDailyDistanceByCarId(String carId, ArrayList<DailyDistanceModel> dailyDistance, CarController.CarListener listener) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("dailyDistance", dailyDistance);

        firestore.collection("Car")
                .document(carId)
                .set(updatedData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    listener.onDailyDistanceUpdate(carId);
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                });
    }
}
