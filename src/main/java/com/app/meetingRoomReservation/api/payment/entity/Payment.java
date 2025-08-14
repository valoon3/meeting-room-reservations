package com.app.meetingRoomReservation.api.payment.entity;

import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "payment")
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_provider_id", nullable = false)
    private PaymentProvider paymentProvider;

    public void addReservations(List<Reservation> reservations) {
        reservations.forEach(this::addReservation);
    }

    public static Payment create(int totalPrice, List<Reservation> reservations) {
        Payment payment = new Payment();
        payment.totalPrice = totalPrice;
        payment.paymentStatusType = PaymentStatusType.PENDING;
        payment.addReservations(reservations);

        return payment;
    }

    // todo: reservation 관계 변경으로 그쪽으로 옮기기

    public void updatePaymentStatus(PaymentStatusType paymentStatusType) {
        this.paymentStatusType = paymentStatusType;

//        if (paymentStatusType == PaymentStatusType.SUCCESS) {
//            this.reservation.updateReservationStatus(ReservationStatusType.RESERVATION_CONFIRMATION);
//        }
//
//        if (paymentStatusType == PaymentStatusType.FAILED) {
//            // 예약 상태 그대로
//        }
//
//        if (paymentStatusType == PaymentStatusType.CANCELLED) {
//            this.reservation.updateReservationStatus(ReservationStatusType.CANCEL);
//        }
    }
    private void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.updatePayment(this);
    }

    public void updatePaymentProvider(PaymentProvider paymentProvider) {
        this.paymentProvider = paymentProvider;
        this.providerType = paymentProvider.getProviderType();
    }
}
