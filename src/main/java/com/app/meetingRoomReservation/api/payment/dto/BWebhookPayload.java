package com.app.meetingRoomReservation.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BWebhookPayload {
    long transactionId;
    long paymentId;
    String status;
    long totalPrice;
    String bMockInformation1;
    String bMockInformation2;
}
