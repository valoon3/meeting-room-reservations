package com.app.wiseaiassignment.api.reservation.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@Getter
public class TimeSlice {
    private LocalDateTime start;
    private LocalDateTime end;

    public static TimeSlice create(LocalDateTime start, LocalDateTime end) {
        return new TimeSlice(start, end);
    }

    private TimeSlice(LocalDateTime start, LocalDateTime end) {
        validReservationTime(start, end);
        this.start = start;
        this.end = end;
    }

    private void validReservationTime(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        if (start.getMinute() % 30 != 0 || end.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("예약 시간은 정시 또는 30분 단위로만 가능합니다.");
        }
    }
}
