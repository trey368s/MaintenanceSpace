package com.example.maintenancespace.controllers.events;

import com.example.maintenancespace.models.events.MaintenanceEventModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaintenanceEventController {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static boolean firstCarFetch = true;
    private static boolean firstUserFetch = true;
    public interface MaintenanceEventListener {
        void onEventFetched(MaintenanceEventModel event);
        void onEventsFetched(ArrayList<MaintenanceEventModel> events);
        void onCreation(String docId);
        void onDelete(String docId);
        void onUpdate(String docId);
        void onFailure(Exception e);
    }
    public static void fetchById(String carId, String maintenanceEventId, MaintenanceEventListener listener) {
        firestore.collection("Car")
                .document(carId)
                .collection("MaintenanceEvent")
                .document(maintenanceEventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        MaintenanceEventModel event = documentSnapshot.toObject(MaintenanceEventModel.class);
                        listener.onEventFetched(event);
                    } else {
                        listener.onFailure(new Exception("Event not found"));
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void fetchAllByCarId(String carId, MaintenanceEventListener listener, boolean force) {
        firestore.collection("Car")
                .document(carId)
                .collection("MaintenanceEvent")
                .get(force || firstCarFetch ? Source.SERVER : Source.DEFAULT)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<MaintenanceEventModel> events = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        MaintenanceEventModel event = document.toObject(MaintenanceEventModel.class);
                        events.add(event);
                    }
                    MaintenanceEventController.firstCarFetch = false;
                    listener.onEventsFetched(events);
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void fetchAllByUserId(String userId, MaintenanceEventListener listener, boolean force) {
        firestore.collection("Car")
                .whereArrayContains("userIds", userId)
                .get(force || firstUserFetch ? Source.SERVER : Source.DEFAULT)
                .addOnSuccessListener(task -> {
                    for(DocumentSnapshot carDocument : task.getDocuments()) {
                        carDocument.getReference()
                                .collection("MaintenanceEvent")
                                .get(force || firstUserFetch ? Source.SERVER : Source.DEFAULT)
                                .addOnSuccessListener(documentSnapshots -> {
                                    ArrayList<MaintenanceEventModel> events = new ArrayList<>();
                                    for(DocumentSnapshot eventDocument : documentSnapshots.getDocuments()) {
                                        MaintenanceEventModel event = eventDocument.toObject(MaintenanceEventModel.class);
                                        event.setId(eventDocument.getId());
                                        events.add(event);
                                    }
                                    MaintenanceEventController.firstUserFetch = false;
                                    listener.onEventsFetched(events);
                                })
                                .addOnFailureListener(e -> listener.onFailure(e));
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void deleteById(String carId, String maintenanceEventId, MaintenanceEventListener listener) {
        firestore.collection("Car")
                .document(carId)
                .collection("MaintenanceEvent")
                .document(maintenanceEventId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onDelete(maintenanceEventId);
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                });
    }

    public static void createByCarId(String carId, MaintenanceEventModel event, MaintenanceEventListener listener) {
        event.setId(carId);

        firestore.collection("Car")
                .document(carId)
                .collection("MaintenanceEvent")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    String docId = documentReference.getId();
                    listener.onCreation(docId);
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public static void updateById(String carId, String maintenanceEventId, MaintenanceEventModel updatedEvent, MaintenanceEventListener listener) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", updatedEvent.getName());
        updatedData.put("description", updatedEvent.getDescription());
        updatedData.put("mileage", updatedEvent.getMileage());
        updatedData.put("date", updatedEvent.getDate());
        updatedData.put("status", updatedEvent.getStatus());

        firestore.collection("Car")
                .document(carId)
                .collection("MaintenanceEvent")
                .document(maintenanceEventId)
                .set(updatedData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    listener.onUpdate(maintenanceEventId);
                })
                .addOnFailureListener(e -> {
                    listener.onFailure(e);
                });
    }

    public static void deleteAllByCarId(String carId) {
        List<DocumentSnapshot> events = firestore.collection("Car")
                .document(carId)
                .collection("MaintenanceEvent")
                .get()
                .getResult()
                .getDocuments();
        for(DocumentSnapshot event : events) {
            event.getReference().delete();
        }

    }
}
