package com.example.maintenancespace.utilities;

public class TimeHelpers {
    public static long convertMinutesToSeconds(int minutes) {
        return minutes * 60;
    }

    public static long convertHoursToSeconds(int hours) {
        return convertMinutesToSeconds(hours * 60);
    }

    public static long convertMillisecondsToSeconds(long milliseconds) {
        return milliseconds / 1000;
    }
}
