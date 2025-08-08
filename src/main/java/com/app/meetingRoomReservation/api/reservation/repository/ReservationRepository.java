package com.app.meetingRoomReservation.api.reservation.repository;

import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
