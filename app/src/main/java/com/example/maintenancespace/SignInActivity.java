package com.example.maintenancespace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maintenancespace.controllers.UserController;
import com.example.maintenancespace.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        Intent startMainActivity = new Intent(this, MainActivity.class);

        if(UserController.isUserSignedIn()) {
            startActivity(startMainActivity);
        }

        EditText emailField = root.findViewById(R.id.editEmail);
        EditText passwordField = root.findViewById(R.id.editPassword);
        Button signInButton = root.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(v -> {
            UserController.signIn(emailField.getText().toString(), passwordField.getText().toString(), new UserController.SignInListener() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    startActivity(startMainActivity);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });
    }
}
