package com.app.meetingRoomReservation.api.paymentProvider.repository;

import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {
}
