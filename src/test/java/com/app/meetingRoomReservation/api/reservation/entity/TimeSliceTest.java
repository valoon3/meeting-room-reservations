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

    @Test
    @DisplayName("30분 단위의 다양한 시간 간격에 대해 정확한 단위를 계산한다")
    void testCalculateDurationUnitsForVariousTimeSpans() {
        // 30분 = 1 단위
        TimeSlice thirtyMinutes = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 10, 30)
        );
        assertEquals(1, thirtyMinutes.getCalculateDurationUnits());

        // 1시간 = 2 단위
        TimeSlice oneHour = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 11, 0)
        );
        assertEquals(2, oneHour.getCalculateDurationUnits());

        // 2시간 = 4 단위
        TimeSlice twoHours = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 12, 0)
        );
        assertEquals(4, twoHours.getCalculateDurationUnits());

        // 4시간 30분 = 9 단위
        TimeSlice fourAndHalfHours = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 9, 30),
                LocalDateTime.of(2024, 6, 1, 14, 0)
        );
        assertEquals(9, fourAndHalfHours.getCalculateDurationUnits());
    }

    @Test
    @DisplayName("시작 시간과 종료 시간이 같으면 예외가 발생한다")
    void testCreateTimeSliceWithSameStartAndEndTime() {
        LocalDateTime sameTime = LocalDateTime.of(2024, 6, 1, 10, 0);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            TimeSlice.create(sameTime, sameTime);
        });

        assertEquals(ErrorType.INCORRECT_TIME_ORDER_REQUEST, exception.getErrorType());
    }

    @Test
    @DisplayName("자정(00:00)과 정오(30분 단위)에도 정상적으로 동작한다")
    void testCreateTimeSliceWithMidnightAndNoonTimes() {
        // 자정부터 30분
        TimeSlice fromMidnight = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 6, 1, 0, 30)
        );
        assertEquals(1, fromMidnight.getCalculateDurationUnits());

        // 정오부터 1시간 30분
        TimeSlice fromNoon = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 12, 0),
                LocalDateTime.of(2024, 6, 1, 13, 30)
        );
        assertEquals(3, fromNoon.getCalculateDurationUnits());

        // 23:30부터 다음날 00:30까지 (날짜 경계를 넘나드는 경우)
        TimeSlice acrossDays = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 23, 30),
                LocalDateTime.of(2024, 6, 2, 0, 30)
        );
        assertEquals(2, acrossDays.getCalculateDurationUnits());
    }

    @Test
    @DisplayName("seconds나 nanoseconds가 0이 아닌 경우에도 분 단위 검증이 정상적으로 동작한다")
    void testCreateTimeSliceWithNonZeroSecondsAndNanoseconds() {
        // 초와 나노초가 있어도 분이 30분 단위라면 정상 동작
        TimeSlice validWithSeconds = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 10, 0, 25, 123456789),
                LocalDateTime.of(2024, 6, 1, 10, 30, 45, 987654321)
        );
        assertEquals(1, validWithSeconds.getCalculateDurationUnits());

        // 분이 30분 단위가 아니면 초와 나노초가 있어도 예외 발생
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            TimeSlice.create(
                    LocalDateTime.of(2024, 6, 1, 10, 15, 30, 0),
                    LocalDateTime.of(2024, 6, 1, 11, 0, 0, 0)
            );
        });
        assertEquals(ErrorType.BAD_TIME_UNIT_REQUEST, exception.getErrorType());
    }

    @Test
    @DisplayName("매우 긴 시간 간격에 대해서도 정확히 계산한다")
    void testCalculateDurationUnitsForLongTimeSpan() {
        // 8시간 = 16 단위
        TimeSlice eightHours = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 9, 0),
                LocalDateTime.of(2024, 6, 1, 17, 0)
        );
        assertEquals(16, eightHours.getCalculateDurationUnits());

        // 24시간 = 48 단위
        TimeSlice twentyFourHours = TimeSlice.create(
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 6, 2, 0, 0)
        );
        assertEquals(48, twentyFourHours.getCalculateDurationUnits());
    }

}
