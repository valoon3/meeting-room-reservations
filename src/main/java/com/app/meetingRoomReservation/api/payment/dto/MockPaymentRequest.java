package com.app.meetingRoomReservation.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MockPaymentRequest {
    private Long paymentId;
    private int totalPrice;
}
