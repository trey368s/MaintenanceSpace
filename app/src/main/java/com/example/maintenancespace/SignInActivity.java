package com.example.maintenancespace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    private Intent startMainActivity;

    final private UserController.SignInListener signInHandler = new UserController.SignInListener() {
        @Override
        public void onSuccess(FirebaseUser user) {
            startActivity(startMainActivity);
        }

        @Override
        public void onFailure(Exception e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.startMainActivity = new Intent(this, MainActivity.class);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        UserController.signOut(getApplicationContext());

        if(UserController.isUserSignedIn()) {
            startActivity(startMainActivity);
        } else {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(UserController.CREDENTIAL_NAME, Context.MODE_PRIVATE);
            String email = sharedPref.getString(UserController.EMAIL_KEY, "");
            String password = sharedPref.getString(UserController.PASS_KEY, "");
            if(!email.equals("") && !password.equals("")) {
                UserController.signIn(getApplicationContext(), email, password, signInHandler);
            }
        }

        EditText emailField = root.findViewById(R.id.editEmail);
        EditText passwordField = root.findViewById(R.id.editPassword);
        Button signInButton = root.findViewById(R.id.signInButton);
        Button signUpButton = binding.signUpButton;

        signInButton.setOnClickListener(v -> {
            UserController.signIn(getApplicationContext(), emailField.getText().toString(), passwordField.getText().toString(), signInHandler);
        });

        signUpButton.setOnClickListener(v -> {
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivity(signUpIntent);
        });
    }
}
