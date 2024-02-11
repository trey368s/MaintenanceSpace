package com.example.maintenancespace.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

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
