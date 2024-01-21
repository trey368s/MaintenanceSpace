package com.example.maintenancespace.models.dailyDistance;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.maintenancespace.utilities.TimeHelpers;
import com.google.firebase.Timestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyDistanceModel implements Parcelable {

    private float dailyDistance;
    private Timestamp date;

    public DailyDistanceModel() {}

    public DailyDistanceModel(float dailyDistance, Timestamp date)
    {
        this.dailyDistance = dailyDistance;
        this.date = date;
    }

    protected DailyDistanceModel(Parcel in) {
        dailyDistance = in.readFloat();
        date = in.readParcelable(Timestamp.class.getClassLoader(), Timestamp.class);
    }

    public static final Creator<DailyDistanceModel> CREATOR = new Creator<DailyDistanceModel>() {
        @Override
        public DailyDistanceModel createFromParcel(Parcel in) {
            return new DailyDistanceModel(in);
        }

        @Override
        public DailyDistanceModel[] newArray(int size) {
            return new DailyDistanceModel[size];
        }
    };

    public static float calculateAverageDistance(ArrayList<DailyDistanceModel> dailyDistanceList) {
        float averageDistance = 0;
        Instant now = Instant.now();

        for(DailyDistanceModel dailyDistance : dailyDistanceList) {
            long nowSinceEpochSec = now.getEpochSecond();

            if (nowSinceEpochSec - dailyDistance.getDate().getSeconds() < TimeHelpers.convertDaysToSeconds(7)) {
                averageDistance += dailyDistance.getDailyDistance();
            }
        }

        return averageDistance;
    }

    public Timestamp getDate() { return this.date; }
    public float getDailyDistance() { return this.dailyDistance; }
    public void setDailyDistance(float newDailyDistance) { this.dailyDistance = newDailyDistance; }
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
    public static ArrayList<DailyDistanceModel> createUpdatedDailyDistance(float distanceIncrease, ArrayList<DailyDistanceModel> dailyDistanceList) {
        ArrayList<DailyDistanceModel> distanceList = dailyDistanceList != null ? dailyDistanceList : new ArrayList<DailyDistanceModel>();
        for (DailyDistanceModel dailyDistanceListItem : distanceList) {
            Timestamp timestampNow = DailyDistanceModel.createTimestamp();

            if (dailyDistanceListItem.getDate().getSeconds() == timestampNow.getSeconds()) {
                float newDailyDistance = dailyDistanceListItem.getDailyDistance() + distanceIncrease;
                dailyDistanceListItem.setDailyDistance(newDailyDistance);
                return distanceList; // We found an entry and updated it, we can break out of this code now
            }
        }

        distanceList.add(0, new DailyDistanceModel(distanceIncrease, DailyDistanceModel.createTimestamp()));
        int entryCount = distanceList.size();
        if (entryCount > 7) {
            distanceList.subList(7, entryCount).clear();
        }

        return distanceList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(dailyDistance);
        dest.writeParcelable(date, flags);
    }
}
