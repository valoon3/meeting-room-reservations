package com.app.meetingRoomReservation.api.payment.entity;

import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "payment"
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatusType paymentStatusType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    public static Payment create(ProviderType providerType, int totalPrice, Reservation reservation) {
        if(totalPrice != reservation.getTotalPrice()) {
            throw new BadRequestException(ErrorType.PAYMENT_PRICE_MISMATCH);
        }

        Payment payment = new Payment();
        payment.providerType = providerType;
        payment.totalPrice = totalPrice;
        payment.paymentStatusType = PaymentStatusType.PENDING;
        payment.reservation = reservation;
        return payment;
    }
}
