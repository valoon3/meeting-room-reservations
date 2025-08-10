package com.app.meetingRoomReservation.api.reservation.repository;

import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import com.app.meetingRoomReservation.api.reservation.entity.TimeSlice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.app.meetingRoomReservation.api.reservation.entity.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<Reservation> getAlreadyReservationMeetingRoom(Long meetingRoomId, Long userId, TimeSlice timeSlice) {
        BooleanExpression overlapTimeExpression = getOverlapTimeExpression(timeSlice);
        BooleanExpression myReservationExpression = getMyReservationExpression(userId);
        BooleanExpression othersPaidReservation = getOtherUserConfirmReservationExpression(userId);

        return queryFactory.selectFrom(reservation)
                .where(
                        reservation.meetingRoom.id.eq(meetingRoomId),
                        overlapTimeExpression,
                        myReservationExpression.or(othersPaidReservation)
                )
                .fetch();
    }

    public List<Reservation> getConfirmReservationMeetingRoom(Long meetingRoomId) {
        return queryFactory.selectFrom(reservation)
                .join(reservation.meetingRoom).fetchJoin()
                .where(
                        reservation.meetingRoom.id.eq(meetingRoomId)
                )
                .fetch();
    }

    private BooleanExpression getOtherUserConfirmReservationExpression(Long userId) {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        BooleanExpression confirmedOrInProgress = reservation.reservationStatusType.in(
                ReservationStatusType.RESERVATION_CONFIRMATION,
                ReservationStatusType.PAYMENT_PROGRESS
        );

        BooleanExpression recentTemporaryReservation = reservation.reservationStatusType.eq(ReservationStatusType.TEMPORARY_RESERVATION)
                .and(reservation.createdAt.after(fifteenMinutesAgo));

        return reservation.userId.ne(userId)
                .and(confirmedOrInProgress.or(recentTemporaryReservation));
    }

    private BooleanExpression getMyReservationExpression(Long userId) {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        return reservation.userId.eq(userId)
                .and(reservation.createdAt.after(fifteenMinutesAgo));
    }

    private BooleanExpression getOverlapTimeExpression(TimeSlice timeSlice) {
        return reservation.timeSlice.timeEnd.gt(timeSlice.getTimeStart())
                .and(reservation.timeSlice.timeStart.lt(timeSlice.getTimeEnd()));
    }

}
