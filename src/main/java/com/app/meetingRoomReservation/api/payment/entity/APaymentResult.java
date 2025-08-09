package com.app.meetingRoomReservation.api.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("A_PAY")
public class APaymentResult extends PaymentResult {
    private String aMockInformation1;
    private String aMockInformation2;
}
