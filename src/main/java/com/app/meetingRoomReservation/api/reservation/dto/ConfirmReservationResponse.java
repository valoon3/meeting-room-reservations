package com.app.meetingRoomReservation.api.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConfirmReservationResponse {
    @Schema(description = "예약 아이디", example = "1")
    private Long id;

    @Schema(description = "미팅룸 아이디", example = "roomA")
    private String meetingRoomName;

    @Schema(description = "미팅룸 인원", example = "4")
    private int capacity;

    @Schema(description = "회의 시작 시간", example = "2025-08-10T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "회의 종료 시간", example = "2025-08-10T11:00:00")
    private LocalDateTime endTime;
}
