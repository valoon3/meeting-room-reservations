package com.app.meetingRoomReservation.api.reservation.entity;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "reservation",
        indexes = {
                @Index(name = "idx_reservation_time_start", columnList = "timeStart")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_reservation_meeting_room_time", columnNames = {"meeting_room", "timeSlice_timeStart", "timeSlice_timeEnd"})
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ReservationStatusType reservationStatusType;

    @Embedded
    private TimeSlice timeSlice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_room_id", nullable = false)
    private MeetingRoom meetingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private Reservation(Long userId, ReservationStatusType reservationStatusType, TimeSlice timeSlice, MeetingRoom meetingRoom) {
        this.userId = userId;
        this.reservationStatusType = reservationStatusType;
        this.timeSlice = timeSlice;
        this.meetingRoom = meetingRoom;
    }

    public static Reservation create(
            Long userId,
            TimeSlice timeSlice,
            MeetingRoom meetingRoom
    ) {
        return new Reservation(
                userId,
                ReservationStatusType.TEMPORARY_RESERVATION,
                timeSlice,
                meetingRoom
        );
    }

    public void updateReservationStatus(ReservationStatusType reservationStatusType) {
        this.reservationStatusType = reservationStatusType;
    }

    public void updatePayment(Payment payment) {
        this.payment = payment;
    }
}
