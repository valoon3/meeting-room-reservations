package com.app.meetingRoomReservation.api.reservation.dto;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private ProviderType providerType;
    private int paymentAmount;
}
