package com.example.maintenancespace;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.maintenancespace.controllers.users.UserController;
import com.example.maintenancespace.services.NotificationService;
import com.example.maintenancespace.ui.cars.CarViewModel;
import com.example.maintenancespace.ui.events.EventViewModel;
import com.example.maintenancespace.utilities.CsvWriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
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
    public static AppCompatActivity activity;

    public static AppCompatActivity viewModelOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        viewModelOwner = this;
        CarViewModel carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        EventViewModel eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_cars, R.id.navigation_events, R.id.navigation_gps, R.id.navigation_reports)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Intent serviceIntent = new Intent(this, NotificationService.class);
        startService(serviceIntent);
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

    private void showSettingsMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.settings));

        popupMenu.getMenuInflater().inflate(R.menu.settings_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            Log.d("Log out", item.toString());
            UserController.signOut(getApplicationContext());
            Intent signOut = new Intent(this, SignInActivity.class);
            signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signOut);
            return true;
        });

        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.repair_shops) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/repair+shops+near+me/"));
            startActivity(intent);
            return true;
        } else if (id == R.id.settings) {
            this.showSettingsMenu();
        }

        return super.onOptionsItemSelected(item);
    }
}