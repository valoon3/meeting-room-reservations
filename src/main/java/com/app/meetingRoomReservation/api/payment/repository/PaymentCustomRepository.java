package com.app.meetingRoomReservation.api.payment.repository;

import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.EntityNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.app.meetingRoomReservation.api.payment.entity.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class PaymentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Payment findByIdWithReservation(Long paymentId) {
        Payment payment1 = queryFactory
                .selectFrom(payment)
                .leftJoin(payment.reservation).fetchJoin()
                .where(payment.id.eq(paymentId))
                .fetchOne();

        if (payment1 == null) {
            throw new EntityNotFoundException(ErrorType.PAYMENT_NOT_FOUND);
        }

        return payment1;
    }

}
