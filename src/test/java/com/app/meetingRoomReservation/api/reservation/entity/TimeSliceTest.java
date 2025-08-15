package com.app.meetingRoomReservation.api.reservation.entity;

import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimeSliceTest {

    @Test
    @DisplayName("유효한 시간으로 TimeSlice를 생성한다")
    void testCreateTimeSliceWithValidTimes() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 1, 11, 30);

        List<TimeSlice> timeSliceList = TimeSlice.createList(start, end);

        assertNotNull(timeSliceList);
        assertEquals(start, timeSliceList.getFirst().getTimeStart());
        assertEquals(end, timeSliceList.getLast().getTimeEnd());
        // Duration: 1 hour 30 minutes = 90 minutes / 30 = 3 units
        assertEquals(3, timeSliceList.size());
    }

    @Test
    @DisplayName("종료 시간이 시작 시간보다 빠르면 예외가 발생한다")
    void testCreateTimeSliceWithIncorrectTimeOrder() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 11, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 1, 10, 0); // 종료 시간이 더 빠름

        // 예외 발생을 확인하고, 발생한 예외 객체를 `exception` 변수에 저장
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            TimeSlice.createList(start, end);
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
            TimeSlice.createList(startWithInvalidMinute, end);
        });
        assertEquals(ErrorType.BAD_TIME_UNIT_REQUEST, exception1.getErrorType());

        // 종료 시간의 분(minute)이 30분 단위가 아닌 경우
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 10, 0);
        LocalDateTime endWithInvalidMinute = LocalDateTime.of(2024, 6, 1, 11, 45);

        BadRequestException exception2 = assertThrows(BadRequestException.class, () -> {
            TimeSlice.createList(start, endWithInvalidMinute);
        });
        assertEquals(ErrorType.BAD_TIME_UNIT_REQUEST, exception2.getErrorType());
    }

    @Test
    @DisplayName("30분 단위의 다양한 시간 간격에 대해 정확한 단위를 계산한다")
    void testCalculateDurationUnitsForVariousTimeSpans() {
        // 30분 = 1 단위
        List<TimeSlice> thirtyMinutes = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 10, 30)
        );
        assertEquals(1, thirtyMinutes.size());

        // 1시간 = 2 단위
        List<TimeSlice> oneHour = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 11, 0)
        );
        assertEquals(2, oneHour.size());

        // 2시간 = 4 단위
        List<TimeSlice> twoHours = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 10, 0),
                LocalDateTime.of(2024, 6, 1, 12, 0)
        );
        assertEquals(4, twoHours.size());

        // 4시간 30분 = 9 단위
        List<TimeSlice> fourAndHalfHours = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 9, 30),
                LocalDateTime.of(2024, 6, 1, 14, 0)
        );
        assertEquals(9, fourAndHalfHours.size());
    }

    @Test
    @DisplayName("시작 시간과 종료 시간이 같으면 예외가 발생한다")
    void testCreateTimeSliceWithSameStartAndEndTime() {
        LocalDateTime sameTime = LocalDateTime.of(2024, 6, 1, 10, 0);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            TimeSlice.createList(sameTime, sameTime);
        });

        assertEquals(ErrorType.INCORRECT_TIME_ORDER_REQUEST, exception.getErrorType());
    }

    @Test
    @DisplayName("자정(00:00)과 정오(30분 단위)에도 정상적으로 동작한다")
    void testCreateTimeSliceWithMidnightAndNoonTimes() {
        // 자정부터 30분
        List<TimeSlice> fromMidnight = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 6, 1, 0, 30)
        );
        assertEquals(1, fromMidnight.size());

        // 정오부터 1시간 30분
        List<TimeSlice> fromNoon = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 12, 0),
                LocalDateTime.of(2024, 6, 1, 13, 30)
        );
        assertEquals(3, fromNoon.size());

        // 23:30부터 다음날 00:30까지 (날짜 경계를 넘나드는 경우)
        List<TimeSlice> acrossDays = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 23, 30),
                LocalDateTime.of(2024, 6, 2, 0, 30)
        );
        assertEquals(2, acrossDays.size());
    }

    @Test
    @DisplayName("매우 긴 시간 간격에 대해서도 정확히 계산한다")
    void testCalculateDurationUnitsForLongTimeSpan() {
        // 8시간 = 16 단위
        List<TimeSlice> eightHours = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 9, 0),
                LocalDateTime.of(2024, 6, 1, 17, 0)
        );
        assertEquals(16, eightHours.size());

        // 24시간 = 48 단위
        List<TimeSlice> twentyFourHours = TimeSlice.createList(
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 6, 2, 0, 0)
        );
        assertEquals(48, twentyFourHours.size());
    }

}
