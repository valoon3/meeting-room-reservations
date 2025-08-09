package com.app.meetingRoomReservation.api.payment.controller;

import com.app.meetingRoomReservation.api.payment.dto.SelectPaymentStatusResponse;
import com.app.meetingRoomReservation.api.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 상태를 조회합니다.", description = "결제 ID에 해당하는 결제의 상태를 조회합니다.")
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<SelectPaymentStatusResponse> selectPaymentStatus(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(paymentId));
    }

}
