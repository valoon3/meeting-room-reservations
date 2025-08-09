package com.app.meetingRoomReservation.api.payment.entity;

import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.ExternalServiceUnavailableException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "provider_type")
public abstract class PaymentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", insertable = false, updatable = false)
    private ProviderType providerType;

    @Column(nullable = false)
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    public PaymentResult(int totalPrice, Payment payment) {
        if(totalPrice != payment.getTotalPrice()) {
            throw new ExternalServiceUnavailableException(ErrorType.PAYMENT_AMOUNT_MISMATCH);
        }

        this.totalPrice = totalPrice;
        this.payment = payment;
        this.providerType = payment.getProviderType();
    }

    public void updatePaymentStatus() {
        // todo: webhook 상태에 따라서 업데이트 로직 정의 추가 필요
        if(payment.getPaymentStatusType().equals(PaymentStatusType.PENDING)) {
            payment.updatePaymentStatus(PaymentStatusType.SUCCESS);
        }
    }

}
