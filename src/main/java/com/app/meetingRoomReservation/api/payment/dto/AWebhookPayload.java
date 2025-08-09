package com.app.meetingRoomReservation.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AWebhookPayload {
    long transactionId;
    long paymentId;
    String status;
    long totalPrice;
    String aMockInformation1;
    String aMockInformation2;
}
