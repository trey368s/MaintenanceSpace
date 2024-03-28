package com.example.maintenancespace.controllers.users;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserController
{
    public interface SignInListener {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

    final public static String EMAIL_KEY = "EMAIL";
    final public static String PASS_KEY = "PASS";

    final public static String CREDENTIAL_NAME = "CREDENTIALS";

    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static void signIn(Context context, String email, String password, SignInListener listener) {
        fireAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = fireAuth.getCurrentUser();
                        saveLoginCredentials(context, email, password);
                        listener.onSuccess(user);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public static void signOut(Context context) {
        fireAuth.signOut();
        clearLoginCredentials(context);
    }

    public static boolean isUserSignedIn() {
        FirebaseUser currentUser = fireAuth.getCurrentUser();
        if(currentUser == null) {
            return false;
        }
        return true;
    }

    public static FirebaseUser getCurrentUser() {
        FirebaseUser currentUser = fireAuth.getCurrentUser();
        return currentUser;
    }

    public static void signUp(Context context, String email, String password, SignInListener listener) {
        fireAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                signIn(context, email, password, listener);
            } else {
                listener.onFailure(task.getException());
            }
        });
    }

    private static void saveLoginCredentials(Context context, String email, String password) {
        SharedPreferences sharedPref = context.getSharedPreferences(CREDENTIAL_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASS_KEY, password);
        editor.apply();
    }

    private static void clearLoginCredentials(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(CREDENTIAL_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
