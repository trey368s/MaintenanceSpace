package com.example.maintenancespace;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.maintenancespace.utilities.CsvWriter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.maintenancespace.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_cars, R.id.navigation_events, R.id.navigation_reports)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CsvWriter.CSV_WRITE_REQUEST_CODE) {
            try {
                // Get the file Uri
                File eventsFile = new File(getFilesDir(), "events.csv");
                Uri eventsFileUri = FileProvider.getUriForFile(this, "com.maintenancespace.files", eventsFile);

                // Establish streams for reading and writing
                InputStream inputStream = getContentResolver().openInputStream(eventsFileUri);
                OutputStream outputStream = getContentResolver().openOutputStream(data.getData());

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter writer = new BufferedWriter(outputStreamWriter);

                // Write the events to the saved file
                for (String line; (line = reader.readLine()) != null; ) {
                    writer.write(line + "\n");
                }

                // Close out everything to prevent memory leaks
                writer.flush();
                writer.close();
                reader.close();
                outputStreamWriter.flush();
                outputStreamWriter.close();
                inputStreamReader.close();
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException | OutOfMemoryError e) {
                Log.e("Report", Log.getStackTraceString(e));
            }
        }
    }
}