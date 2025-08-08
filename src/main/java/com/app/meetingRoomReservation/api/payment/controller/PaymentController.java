package com.app.meetingRoomReservation.api.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    // 결제 상태 조회
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<Long> get(@PathVariable Long paymentId) {
        return ResponseEntity.ok(1L);
    }

}
