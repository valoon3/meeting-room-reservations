package com.app.meetingRoomReservation.api.paymentProvider.repository;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.app.meetingRoomReservation.api.paymentProvider.entity.QPaymentProvider.paymentProvider;

@Repository
@RequiredArgsConstructor
public class PaymentProviderCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PaymentProvider findByProviderType(ProviderType providerType) {
        return queryFactory.selectFrom(paymentProvider)
                .where(paymentProvider.providerType.eq(providerType))
                .fetchOne();
    }

}
