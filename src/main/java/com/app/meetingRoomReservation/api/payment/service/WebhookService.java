package com.app.meetingRoomReservation.api.payment.service;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.dto.AWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.BWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.CWebhookPayload;
import com.app.meetingRoomReservation.api.payment.dto.PaymentResultDto;
import com.app.meetingRoomReservation.api.payment.entity.*;
import com.app.meetingRoomReservation.api.payment.repository.PaymentCustomRepository;
import com.app.meetingRoomReservation.api.payment.repository.PaymentRepository;
import com.app.meetingRoomReservation.api.payment.repository.PaymentResultRepository;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.ExternalServiceUnavailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebhookService {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final PaymentResultRepository paymentResultRepository;
    private final PaymentCustomRepository paymentCustomRepository;

    @Transactional
    public void processPaymentWebhook(ProviderType provider, Map<String, Object> payload) {
        PaymentResult paymentResult = getPaymentResult(provider, payload);
        paymentResult.updatePaymentStatus();
    }

    private PaymentResult getPaymentResult(ProviderType provider, Map<String, Object> payload) {
        PaymentResult paymentResult;
        switch (provider) {
            case A_PAY -> {
                AWebhookPayload aPayload = objectMapper.convertValue(payload, AWebhookPayload.class);
                Payment payment = findPaymentById(aPayload.paymentId());
                paymentResult = createResult(aPayload, payment);
            }
            case B_PAY -> {
                BWebhookPayload bPayload = objectMapper.convertValue(payload, BWebhookPayload.class);
                Payment payment = findPaymentById(bPayload.paymentId());
                paymentResult = createResult(bPayload, payment);
            }
            case C_PAY -> {
                CWebhookPayload cPayload = objectMapper.convertValue(payload, CWebhookPayload.class);
                Payment payment = findPaymentById(cPayload.paymentId());
                paymentResult = createResult(cPayload, payment);
            }
            default -> throw new ExternalServiceUnavailableException(ErrorType.PAYMENT_PROVIDER_NOT_SUPPORTED);
        }
        return paymentResult;
    }

    /**
     * A사 웹훅 처리: A사 결과(APaymentResult)를 생성하고 상태를 업데이트합니다.
     */
    private PaymentResult createResult(AWebhookPayload payload, Payment payment) {
        APaymentResult result = APaymentResult.create(
                (int) payload.totalPrice(),
                payload.status(),
                payload.aMockInformation1(),
                payload.aMockInformation2(),
                payment
        );

        return paymentResultRepository.save(result);
    }

    /**
     * B사 웹훅 처리: B사 결과(BPaymentResult)를 생성하고 상태를 업데이트합니다.
     */
    private PaymentResult createResult(BWebhookPayload payload, Payment payment) {
        BPaymentResult result = BPaymentResult.create(
                (int) payload.totalPrice(),
                payload.status(),
                payload.bMockInformation1(),
                payload.bMockInformation2(),
                payment
        );

        return paymentResultRepository.save(result);
    }

    /**
     * C사 웹훅 처리: C사 결과(CPaymentResult)를 생성하고 상태를 업데이트합니다.
     */
    private PaymentResult createResult(CWebhookPayload payload, Payment payment) {
        CPaymentResult result = CPaymentResult.create(
                (int) payload.totalPrice(),
                payload.status(),
                payload.cMockInformation1(),
                payload.cMockInformation2(),
                payment
        );

        return paymentResultRepository.save(result);
    }

    private Payment findPaymentById(Long paymentId) {
        return paymentCustomRepository.findByIdWithReservation(paymentId);
    }

}
