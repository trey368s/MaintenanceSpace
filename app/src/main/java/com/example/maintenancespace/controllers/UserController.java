package com.example.maintenancespace.controllers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;



public class UserController
{
    public interface SignInListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static void signIn(String email, String password, SignInListener listener) {
        fireAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = fireAuth.getCurrentUser();
                        listener.onSuccess(user);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public static boolean isUserSignedIn() {
        FirebaseUser currentUser = fireAuth.getCurrentUser();
        if(currentUser != null) {
            return false;
        }
        return true;
    }

    public static FirebaseUser getCurrentUser() {
        FirebaseUser currentUser = fireAuth.getCurrentUser();
        return currentUser;
    }

}
