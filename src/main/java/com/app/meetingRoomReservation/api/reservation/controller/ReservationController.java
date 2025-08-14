package com.app.meetingRoomReservation.api.reservation.controller;

import com.app.meetingRoomReservation.api.reservation.dto.ConfirmReservationResponse;
import com.app.meetingRoomReservation.api.reservation.dto.CreateReservationRequest;
import com.app.meetingRoomReservation.api.reservation.dto.PaymentRequest;
import com.app.meetingRoomReservation.api.reservation.service.ReservationService;
import com.app.meetingRoomReservation.response.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "회의실 예약 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약을 생성합니다.", description = "대기 상태의 결제되지 않은 예약을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예약 생성 성공",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (시간 중복, 규칙 위반 등)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회의실",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/meeting-room/{id}")
    public ResponseEntity<Long> createReservation(@PathVariable Long id, @Validated @RequestBody CreateReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(id, request));
    }

    @Operation(summary = "예약을 조회합니다.", description = "해당 미팅룸의 확정된 예약을 조회합니다.")
    @GetMapping("/meeting-room/{id}")
    public ResponseEntity<List<ConfirmReservationResponse>> selectConfirmReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.selectConfirmReservations(id));
    }

    @Operation(summary = "예약 결제를 처리합니다.", description = "예약 결제 처리.")
    @PostMapping("/{id}/payment")
    public void payment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        // 결제 생성 로직 구현
        reservationService.processPayment(id, request);
    }

    @Operation(summary = "예약을 취소합니다.", description = "해당 미팅룸의 확정된 예약을 취소합니다.")
    @DeleteMapping("/meeting-room/{id}")
    public ResponseEntity<Long> deleteReservation(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(1L);
    }
}
