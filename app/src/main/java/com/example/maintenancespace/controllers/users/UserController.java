package com.example.maintenancespace.controllers.users;
import android.app.Activity;
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

    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static void signIn(Activity activity, String email, String password, SignInListener listener) {
        fireAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser user = fireAuth.getCurrentUser();
                        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(EMAIL_KEY, email);
                        editor.putString(PASS_KEY, password);
                        editor.apply();
                        listener.onSuccess(user);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public static void signOut() {
        fireAuth.signOut();
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

}
