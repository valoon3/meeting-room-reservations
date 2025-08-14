package com.app.meetingRoomReservation.api.reservation.entity;

import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
@Getter
public class TimeSlice {
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    // 30분 단위 TimeSlice 리스트를 생성하는 정적 메서드
    public static List<TimeSlice> createList(LocalDateTime start, LocalDateTime end) {
        validReservationTime(start, end);

        List<TimeSlice> timeSlices = new ArrayList<>();

        for (LocalDateTime currentStart = start; currentStart.isBefore(end); currentStart = currentStart.plusMinutes(30)) {
            LocalDateTime currentEnd = currentStart.plusMinutes(30);
            timeSlices.add(new TimeSlice(currentStart, currentEnd));
        }

        return timeSlices;
    }

    // 30분을 하나의 단위로 사용
    public int getCalculateDurationUnits() {
        Duration duration = Duration.between(timeStart, timeEnd);
        long totalMinutes = duration.toMinutes();
        return (int) (totalMinutes / 30);
    }

    private TimeSlice(LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    private static void validReservationTime(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new BadRequestException(ErrorType.INCORRECT_TIME_ORDER_REQUEST);
        }

        if (start.getMinute() % 30 != 0 || end.getMinute() % 30 != 0) {
            throw new BadRequestException(ErrorType.BAD_TIME_UNIT_REQUEST);
        }
    }
}
