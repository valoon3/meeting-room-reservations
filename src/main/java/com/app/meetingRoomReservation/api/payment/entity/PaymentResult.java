package com.app.meetingRoomReservation.api.payment.entity;

import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
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

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    public PaymentResult(int totalPrice, String status, Payment payment) {
        if(totalPrice != payment.getTotalPrice()) {
            throw new ExternalServiceUnavailableException(ErrorType.PAYMENT_AMOUNT_MISMATCH);
        }

        this.totalPrice = totalPrice;
        this.status = status;
        this.payment = payment;
        this.providerType = payment.getProviderType();
    }

    public void updatePaymentStatus() {
        if(this.status.equals("SUCCESS")) {
            payment.updatePaymentStatus(PaymentStatusType.SUCCESS);
            payment.getReservation().updateReservationStatus(ReservationStatusType.RESERVATION_CONFIRMATION);
        }
        if(this.status.equals("FAILED")) {
            payment.updatePaymentStatus(PaymentStatusType.FAILED);
            payment.getReservation().updateReservationStatus(ReservationStatusType.PAYMENT_PROGRESS);
        }
        if(this.status.equals("PENDING")) {
            payment.updatePaymentStatus(PaymentStatusType.PENDING);
            payment.getReservation().updateReservationStatus(ReservationStatusType.PAYMENT_PROGRESS);
        }
    }

}
