package com.app.wiseaiassignment.api.reservation.repository;

import com.app.wiseaiassignment.api.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
