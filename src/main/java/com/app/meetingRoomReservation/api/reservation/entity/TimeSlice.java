package com.app.meetingRoomReservation.api.reservation.entity;

import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@Getter
public class TimeSlice {
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    public static TimeSlice create(LocalDateTime start, LocalDateTime end) {
        return new TimeSlice(start, end);
    }

    // 30분을 하나의 단위로 사용
    public int getCalculateDurationUnits() {
        Duration duration = Duration.between(timeStart, timeEnd);
        long totalMinutes = duration.toMinutes();
        return (int) (totalMinutes / 30);
    }

    private TimeSlice(LocalDateTime timeStart, LocalDateTime timeEnd) {
        validReservationTime(timeStart, timeEnd);
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    private void validReservationTime(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new BadRequestException(ErrorType.INCORRECT_TIME_ORDER_REQUEST);
        }

        if (start.getMinute() % 30 != 0 || end.getMinute() % 30 != 0) {
            throw new BadRequestException(ErrorType.BAD_TIME_UNIT_REQUEST);
        }
    }
}
