package com.app.meetingRoomReservation.api.payment.service;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.dto.AWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.BWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.CWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.PaymentResultDto;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.payment.repository.PaymentRepository;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.paymentProvider.repository.PaymentProviderRepository;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.EntityNotFoundException;
import com.app.meetingRoomReservation.error.exceptions.ExternalServiceUnavailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookService {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private PaymentProviderRepository paymentProviderRepository;

    public void processPaymentWebhook(ProviderType provider, Map<String, Object> payload) {
        PaymentResultDto resultDto;

        // provider 타입에 따라 각기 다른 DTO로 변환
        switch (provider) {
            case A_PAY:
                AWebhookPayload aPayload = objectMapper.convertValue(payload, AWebhookPayload.class);
                resultDto = new PaymentResultDto(aPayload.getPaymentId(), aPayload.getStatus());
                break;
            case B_PAY:
                BWebhookPayload bPayload = objectMapper.convertValue(payload, BWebhookPayload.class);
                resultDto = new PaymentResultDto(bPayload.getPaymentId(), bPayload.getStatus());
                break;
            case C_PAY:
                CWebhookPayload cPayload = objectMapper.convertValue(payload, CWebhookPayload.class);
                resultDto = new PaymentResultDto(cPayload.getPaymentId(), cPayload.getStatus());
                break;
            default:
                throw new ExternalServiceUnavailableException(ErrorType.PAYMENT_PROVIDER_NOT_SUPPORTED);
        }

        updatePaymentAndReservationStatus(resultDto);
    }

    private void updatePaymentAndReservationStatus(PaymentResultDto result) {
        Payment payment = paymentRepository.findById(result.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorType.PAYMENT_NOT_FOUND));

        PaymentProvider paymentProvider = paymentProviderRepository.findByProviderType(payment.getProviderType())
                .orElseThrow(() -> new ExternalServiceUnavailableException(ErrorType.PAYMENT_PROVIDER_NOT_SUPPORTED));

        // todo: 결제 상태에 따라 예약 상태 업데이트 로직 추가 (paymentResult entity 만든 후 수정)
    }

}
