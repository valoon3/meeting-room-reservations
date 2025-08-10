package com.app.meetingRoomReservation.api.payment.dto;

public record AWebhookPayload (
    long transactionId,
    long paymentId,
    String status,
    long totalPrice,
    String aMockInformation1,
    String aMockInformation2
) {}
