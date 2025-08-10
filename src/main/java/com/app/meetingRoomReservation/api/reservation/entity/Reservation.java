package com.app.meetingRoomReservation.api.reservation.entity;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "reservation",
        indexes = {
                @Index(name = "idx_reservation_time_start", columnList = "timeStart")
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

    @Column(nullable = false)
    private int totalPrice;

    @Embedded
    private TimeSlice timeSlice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_room_id", nullable = false)
    private MeetingRoom meetingRoom;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private Reservation(Long userId, ReservationStatusType reservationStatusType, int totalPrice, TimeSlice timeSlice, MeetingRoom meetingRoom) {
        this.userId = userId;
        this.reservationStatusType = reservationStatusType;
        this.totalPrice = totalPrice;
        this.timeSlice = timeSlice;
        this.meetingRoom = meetingRoom;
    }

    public static Reservation create(
            Long userId,
            int totalPrice,
            TimeSlice timeSlice,
            MeetingRoom meetingRoom
    ) {
        return new Reservation(
                userId,
                ReservationStatusType.TEMPORARY_RESERVATION,
                totalPrice,
                timeSlice,
                meetingRoom
        );
    }

    public void updateReservationStatus(ReservationStatusType reservationStatusType) {
        this.reservationStatusType = reservationStatusType;
    }

}
