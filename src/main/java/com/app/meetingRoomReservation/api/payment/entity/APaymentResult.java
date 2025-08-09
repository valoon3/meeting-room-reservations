package com.app.meetingRoomReservation.api.payment.entity;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
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

    private APaymentResult(String info1, String info2, Payment payment) {
        super(payment);
        this.aMockInformation1 = info1;
        this.aMockInformation2 = info2;
    }

    public static APaymentResult create(String info1, String info2, Payment payment) {
        return new APaymentResult(info1, info2, payment);
    }
}
