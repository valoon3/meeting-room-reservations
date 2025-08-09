package com.app.meetingRoomReservation.api.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@DiscriminatorValue("B_PAY")
public class BPaymentResult extends PaymentResult {
    private String bMockInformation1;
    private String bMockInformation2;

    private BPaymentResult(String info1, String info2, Payment payment) {
        super(payment);
        this.bMockInformation1 = info1;
        this.bMockInformation2 = info2;
    }

    public static BPaymentResult create(String info1, String info2, Payment payment) {
        return new BPaymentResult(info1, info2, payment);
    }
}
