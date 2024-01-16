package com.example.maintenancespace.models.dailyMileage;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.maintenancespace.utilities.TimeHelpers;
import com.google.firebase.Timestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyMileageModel implements Parcelable {

    private float dailyMileage;
    private Timestamp date;

    public DailyMileageModel() {}

    public DailyMileageModel(float dailyMileage, Timestamp date)
    {
        this.dailyMileage = dailyMileage;
        this.date = date;
    }

    protected DailyMileageModel(Parcel in) {
        dailyMileage = in.readFloat();
        date = in.readParcelable(Timestamp.class.getClassLoader(), Timestamp.class);
    }

    public static final Creator<DailyMileageModel> CREATOR = new Creator<DailyMileageModel>() {
        @Override
        public DailyMileageModel createFromParcel(Parcel in) {
            return new DailyMileageModel(in);
        }

        @Override
        public DailyMileageModel[] newArray(int size) {
            return new DailyMileageModel[size];
        }
    };

    public static float calculateAverageMileage(ArrayList<DailyMileageModel> dailyMileageList) {
        float averageMileage = 0;
        Instant now = Instant.now();

        for(DailyMileageModel dailyMileage : dailyMileageList) {
            long nowSinceEpochSec = now.getEpochSecond();

            if (nowSinceEpochSec - dailyMileage.getDate().getSeconds() < TimeHelpers.convertDaysToSeconds(7)) {
                averageMileage += dailyMileage.getDailyMileage();
            }
        }

        return averageMileage;
    }

    public Timestamp getDate() { return this.date; }
    public float getDailyMileage() { return this.dailyMileage; }
    public void setDailyMileage(float newDailyMileage) { this.dailyMileage = newDailyMileage; }
    public static Timestamp createTimestamp() {
        Instant utcNow = Instant.now();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeHelpers.convertSecondsToMilliseconds(utcNow.getEpochSecond()));
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long secondsSinceEpoch = TimeHelpers.convertMillisecondsToSeconds(calendar.getTimeInMillis());
        return new Timestamp(secondsSinceEpoch, 0);
    };
    public static void updateDailyMileage(float mileageIncrease, ArrayList<DailyMileageModel> dailyMileageList) {
        for (DailyMileageModel dailyMileageListItem : dailyMileageList) {
            Timestamp timestampNow = DailyMileageModel.createTimestamp();

            if (dailyMileageListItem.getDate().getSeconds() == timestampNow.getSeconds()) {
                float newDailyMileage = dailyMileageListItem.getDailyMileage() + mileageIncrease;
                dailyMileageListItem.setDailyMileage(newDailyMileage);
                return; // We found an entry and updated it, we can break out of this code now
            }
        }

        dailyMileageList.add(0, new DailyMileageModel(mileageIncrease, DailyMileageModel.createTimestamp()));
        int entryCount = dailyMileageList.size();
        if (entryCount > 7) {
            dailyMileageList.subList(7, entryCount).clear();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(dailyMileage);
        dest.writeParcelable(date, flags);
    }
}
