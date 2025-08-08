package com.app.meetingRoomReservation.api.reservation.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSliceTest {

    @Test
    void testCreateTimeSliceWithValidTimes() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 1, 11, 30);

        TimeSlice timeSlice = TimeSlice.create(start, end);

        assertNotNull(timeSlice);
        assertEquals(start, timeSlice.getTimeStart());
        assertEquals(end, timeSlice.getTimeEnd());
        // Duration: 1 hour 30 minutes = 90 minutes / 30 = 3 units
        assertEquals(3, timeSlice.getCalculateDurationUnits());
    }
}
