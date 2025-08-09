package com.app.meetingRoomReservation.api.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("C_PAY")
public class CPaymentResult extends PaymentResult {
    private String cMockInformation1;
    private String cMockInformation2;
}
