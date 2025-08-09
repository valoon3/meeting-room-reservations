package com.app.meetingRoomReservation.api.payment.service;

import com.app.meetingRoomReservation.api.payment.dto.SelectPaymentStatusResponse;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.payment.repository.PaymentCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentCustomRepository paymentCustomRepository;

    public SelectPaymentStatusResponse getPaymentStatus(Long paymentId) {
        Payment byIdWithReservation = paymentCustomRepository.findByIdWithReservation(paymentId);

        return new SelectPaymentStatusResponse(
                byIdWithReservation.getId(),
                byIdWithReservation.getProviderType(),
                byIdWithReservation.getTotalPrice(),
                byIdWithReservation.getPaymentStatusType()
        );
    }

}
