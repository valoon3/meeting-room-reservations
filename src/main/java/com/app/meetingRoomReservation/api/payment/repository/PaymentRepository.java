package com.app.meetingRoomReservation.api.payment.repository;

import com.app.meetingRoomReservation.api.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
