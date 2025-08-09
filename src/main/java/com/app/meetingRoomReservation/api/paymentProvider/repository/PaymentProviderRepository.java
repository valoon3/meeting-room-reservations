package com.app.meetingRoomReservation.api.paymentProvider.repository;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentProviderRepository extends JpaRepository<PaymentProvider, Long> {
    Optional<PaymentProvider> findByProviderType(ProviderType providerType);
}
