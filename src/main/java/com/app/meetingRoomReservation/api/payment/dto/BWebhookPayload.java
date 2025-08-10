package com.app.meetingRoomReservation.api.payment.dto;


public record BWebhookPayload (
    long transactionId,
    long paymentId,
    String status,
    long totalPrice,
    String bMockInformation1,
    String bMockInformation2
) {}
