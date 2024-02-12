package com.example.maintenancespace;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maintenancespace.databinding.ActivityCarEditBinding;
import com.example.maintenancespace.ui.cars.CarFragment;

import java.lang.ref.WeakReference;

public class CarEditActivity extends AppCompatActivity {

    public static WeakReference<CarFragment> carFragmentWeakReference;
    private ActivityCarEditBinding binding;

    private boolean validateForm(String vin, String make, String model, String trim, int year) {
        if(vin.isEmpty()) {
            return false;
        }
        if(make.isEmpty()) {
            return false;
        }
        if(model.isEmpty()) {
            return false;
        }
        if(trim.isEmpty()) {
            return false;
        }

        return true;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
