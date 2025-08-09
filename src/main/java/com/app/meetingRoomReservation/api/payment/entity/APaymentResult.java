package com.app.meetingRoomReservation.api.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("A_PAY")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class APaymentResult extends PaymentResult {
    private String aMockInformation1;
    private String aMockInformation2;

    private APaymentResult(int totalPrice, String info1, String info2, Payment payment) {
        super(totalPrice, payment);
        this.aMockInformation1 = info1;
        this.aMockInformation2 = info2;
    }

    public static APaymentResult create(int totalPrice, String info1, String info2, Payment payment) {
        return new APaymentResult(totalPrice, info1, info2, payment);
    }
}
