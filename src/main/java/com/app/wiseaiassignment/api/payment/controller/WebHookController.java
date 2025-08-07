package com.app.wiseaiassignment.api.payment.controller;

import com.app.wiseaiassignment.api.payment.constant.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks")
public class WebHookController {

    // 결제사별 웹훅 수신
    @PostMapping("/payments/{provider}")
    public ResponseEntity<Long> receiveWebhook(@PathVariable ProviderType provider) {
        return ResponseEntity.ok(1L);
    }

}
