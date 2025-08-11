package com.app.meetingRoomReservation.api.payment.gateway;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.dto.MockPaymentRequest;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BPayGateway implements PaymentGateway {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${payment-mock-server.b-pay-url}")
    private String apiUrl;

    @Override
    public void requestPayment(Payment payment) {
        MockPaymentRequest request = new MockPaymentRequest(payment.getId(), payment.getTotalPrice());
        restTemplate.postForEntity(apiUrl, request, Void.class);
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.B_PAY;
    }

}
