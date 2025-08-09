package com.app.meetingRoomReservation.api.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("B_PAY")
public class BPaymentResult extends PaymentResult {
    private String bMockInformation1;
    private String bMockInformation2;
}
