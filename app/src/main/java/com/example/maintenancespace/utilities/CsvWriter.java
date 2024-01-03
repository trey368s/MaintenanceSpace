package com.example.maintenancespace.utilities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.maintenancespace.models.events.MaintenanceEventModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvWriter {
    public final static int CSV_WRITE_REQUEST_CODE = 1;
    public static void generateMaintenanceReports(Activity activity,ArrayList<MaintenanceEventModel> events) {
        try {
            // Create a new intent for creating a file
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_TITLE, "events.csv");
            intent.setType("text/csv");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Create a holding file for the data
            File eventsFile = new File(activity.getFilesDir(), "events.csv");
            //Create a writer and write the data to the file
            FileWriter fileWriter = new FileWriter(eventsFile, false);
            fileWriter.write(MaintenanceEventModel.getCsvHeaders() + "\n");
            for (MaintenanceEventModel event : events) {
                fileWriter.write(event.getCsvRow() + "\n");
            }

            // Close out the writer
            fileWriter.flush();
            fileWriter.close();

            // Start the activity to save the file
            activity.startActivityForResult(intent, CSV_WRITE_REQUEST_CODE);
        } catch (IOException e) {
            Log.e("Error", Log.getStackTraceString(e));
        }
    }
}
