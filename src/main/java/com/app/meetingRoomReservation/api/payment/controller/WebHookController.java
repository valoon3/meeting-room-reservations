package com.app.meetingRoomReservation.api.payment.controller;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.dto.AWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.BWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.CWebhookPayload;
import com.app.meetingRoomReservation.api.payment.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks")
public class WebHookController {

    private final WebhookService webhookService;

    @Operation(summary = "결제사 웹훅 공통 수신", description = "모든 결제사로부터 결제 완료/실패 웹훅을 수신합니다.")
    @PostMapping("/payments/{provider}")
    public ResponseEntity<Void> receiveWebhook(
            @PathVariable ProviderType provider,
            @RequestBody Map<String, Object> payload
    ) {
        webhookService.processPaymentWebhook(provider, payload);
        return ResponseEntity.ok().build();
    }

}
