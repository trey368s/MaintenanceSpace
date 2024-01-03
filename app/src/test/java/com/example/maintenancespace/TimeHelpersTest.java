package com.example.maintenancespace;

import static org.junit.Assert.assertEquals;

import com.example.maintenancespace.utilities.TimeHelpers;

import org.junit.Test;

public class TimeHelpersTest {
    @Test
    public void shouldConvertMinutesToCorrectSeconds() {
        assertEquals(TimeHelpers.convertMinutesToSeconds(5), 300);
    }

    @Test
    public void shouldConvertHoursToCorrectSeconds() {
        assertEquals(TimeHelpers.convertHoursToSeconds(2), 7200);
    }

    @Test
    public void shouldConvertMillisecondsToCorrectSeconds() {
        assertEquals(TimeHelpers.convertMillisecondsToSeconds(56000), 56);
    }
}
