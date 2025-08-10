package com.app.meetingRoomReservation.api.payment.dto;

public record CWebhookPayload (
    long transactionId,
    long paymentId,
    String status,
    long totalPrice,
    String cMockInformation1,
    String cMockInformation2
) {}
