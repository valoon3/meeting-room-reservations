package com.app.meetingRoomReservation.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CWebhookPayload {
    long transactionId;
    long paymentId;
    String status;
    long totalPrice;
    String cMockInformation1;
    String cMockInformation2;
}
