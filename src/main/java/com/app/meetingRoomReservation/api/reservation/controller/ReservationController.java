package com.app.meetingRoomReservation.api.reservation.controller;

import com.app.meetingRoomReservation.api.reservation.dto.CreateReservationRequest;
import com.app.meetingRoomReservation.api.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회의실 예약 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약을 생성합니다.", description = "대기 상태의 결제되지 않은 예약을 생성합니다.")
    @PostMapping("/meeting-room/{id}")
    public ResponseEntity<Long> createReservation(@PathVariable Long id, @Validated @RequestBody CreateReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(id, request));
    }

    @Operation(summary = "예약을 조회합니다.", description = "해당 미팅룸의 확정된 예약을 조회합니다.")
    @GetMapping("/meeting-room/{id}")
    public ResponseEntity<Long> selectReservation(@PathVariable Long id) {
        return ResponseEntity.ok(1L);
    }

    // 예약 취소
    @Operation(summary = "예약을 취소합니다.", description = "해당 미팅룸의 확정된 예약을 취소합니다.")
    @DeleteMapping("/meeting-room/{id}")
    public ResponseEntity<Long> deleteReservation(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(1L);
    }

    // 결제 처리
    @Operation(summary = "예약 결제를 처리합니다.", description = "웹훅 결과와 비교 후 예약을 확정처리합니다.")
    @PostMapping("/{id}/payment")
    public ResponseEntity<Long> payment(@PathVariable Long id) {
        // 결제 생성 로직 구현
        return ResponseEntity.ok(1L);
    }
}
