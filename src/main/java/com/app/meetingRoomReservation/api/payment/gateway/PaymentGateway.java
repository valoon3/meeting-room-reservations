package com.app.meetingRoomReservation.api.payment.gateway;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.entity.Payment;

public interface PaymentGateway {
    void requestPayment(Payment payment);

    // 자신이 어떤 결제사 타입인지 반환
    ProviderType getProviderType();
}
