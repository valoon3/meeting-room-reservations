package com.app.meetingRoomReservation.api.reservation.entity;

import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSliceTest {

    @Test
    @DisplayName("유효한 시간으로 TimeSlice를 생성한다")
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

    @Test
    @DisplayName("종료 시간이 시작 시간보다 빠르면 예외가 발생한다")
    void testCreateTimeSliceWithIncorrectTimeOrder() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 11, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 1, 10, 0); // 종료 시간이 더 빠름

        // 예외 발생을 확인하고, 발생한 예외 객체를 `exception` 변수에 저장
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            TimeSlice.create(start, end);
        });

        // 발생한 예외의 ErrorType이 예상과 일치하는지 확인
        assertEquals(ErrorType.INCORRECT_TIME_ORDER_REQUEST, exception.getErrorType());
    }

    @Test
    @DisplayName("시간이 30분 단위가 아니면 예외가 발생한다")
    void testCreateTimeSliceWithInvalidTimeUnit() {
        // 시작 시간의 분(minute)이 30분 단위가 아닌 경우
        LocalDateTime startWithInvalidMinute = LocalDateTime.of(2024, 6, 1, 10, 15);
        LocalDateTime end = LocalDateTime.of(2024, 6, 1, 11, 0);

        BadRequestException exception1 = assertThrows(BadRequestException.class, () -> {
            TimeSlice.create(startWithInvalidMinute, end);
        });
        assertEquals(ErrorType.BAD_TIME_UNIT_REQUEST, exception1.getErrorType());

        // 종료 시간의 분(minute)이 30분 단위가 아닌 경우
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 10, 0);
        LocalDateTime endWithInvalidMinute = LocalDateTime.of(2024, 6, 1, 11, 45);

        BadRequestException exception2 = assertThrows(BadRequestException.class, () -> {
            TimeSlice.create(start, endWithInvalidMinute);
        });
        assertEquals(ErrorType.BAD_TIME_UNIT_REQUEST, exception2.getErrorType());
    }

}
