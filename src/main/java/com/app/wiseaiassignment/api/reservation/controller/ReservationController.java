package com.app.wiseaiassignment.api.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    // 예약 생성
    @PostMapping("/meeting-room/{id}")
    public ResponseEntity<Long> createReservations(@PathVariable Long id) {
        return ResponseEntity.ok(1L);
    }

    // 전체 미팅룸 예약 조회
    @GetMapping("/meeting-room")
    public ResponseEntity<List> selectReservations() {
        return ResponseEntity.ok(List.of());
    }

    // 단일 미팅룸 예약 조회
    @GetMapping("/meeting-room/{id}")
    public ResponseEntity<Long> selectReservation(@PathVariable Long id) {
        return ResponseEntity.ok(1L);
    }

    // 예약 취소
    @DeleteMapping("/meeting-room/{id}")
    public ResponseEntity<Long> deleteReservation(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(1L);
    }

    // 결제 처리
    @PostMapping("/{id}/payment")
    public ResponseEntity<Long> payment(@PathVariable Long id) {
        // 결제 생성 로직 구현
        return ResponseEntity.ok(1L);
    }
}
