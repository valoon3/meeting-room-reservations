package com.app.meetingRoomReservation.api.payment.repository;

import com.app.meetingRoomReservation.api.payment.entity.PaymentResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentResultRepository extends JpaRepository<PaymentResult, Long> {
}
