package com.app.meetingRoomReservation.api.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {

    @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
    @Schema(description = "유저 아이디", example = "1")
    private Long userId;

    @NotNull(message = "회의실 시작시간은 필수 입력 값입니다.")
    @Schema(description = "회의 시작 시간", example = "2025-08-10T10:00:00")
    private LocalDateTime startTime;

    @NotNull(message = "회의실 종료시간은 필수 입력 값입니다.")
    @Schema(description = "회의 종료 시간", example = "2025-08-10T11:00:00")
    private LocalDateTime endTime;
}
