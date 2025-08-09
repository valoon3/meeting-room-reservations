package com.app.meetingRoomReservation.api.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@DiscriminatorValue("C_PAY")
public class CPaymentResult extends PaymentResult {
    private String cMockInformation1;
    private String cMockInformation2;

    private CPaymentResult(String info1, String info2, Payment payment) {
        super(payment);
        this.cMockInformation1 = info1;
        this.cMockInformation2 = info2;
    }

    public static CPaymentResult create(String info1, String info2, Payment payment) {
        return new CPaymentResult(info1, info2, payment);
    }
}
