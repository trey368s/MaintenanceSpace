package com.example.maintenancespace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.databinding.ActivitySignInBinding;
import com.example.maintenancespace.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        EditText emailField = root.findViewById(R.id.editEmail);
        EditText passwordField = binding.editPassword;
        EditText confirmPasswordField = binding.passwordConfirm;
        Button signUpButton = binding.submitButton;

        signUpButton.setOnClickListener(v -> {
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();
            if(password.equals(confirmPassword)) {
                UserController.signUp(getApplicationContext(), emailField.getText().toString(), passwordField.getText().toString(), new UserController.SignInListener() {
                    @Override
                    public void onSuccess(FirebaseUser user) {
                        Intent startMainActivity = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(startMainActivity);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
