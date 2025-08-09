package com.app.meetingRoomReservation.api.payment.dto;

import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SelectPaymentStatusResponse {
    private Long id;
    private ProviderType providerType;
    private int totalPrice;
    private PaymentStatusType paymentStatusType;
}
