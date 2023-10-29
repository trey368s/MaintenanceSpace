package com.example.maintenancespace.ui.cars;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a car fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}