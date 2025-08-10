package com.app.meetingRoomReservation.api.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationRequest {
    private Long id;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;
}
