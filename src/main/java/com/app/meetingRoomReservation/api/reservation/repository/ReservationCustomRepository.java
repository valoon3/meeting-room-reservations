package com.app.meetingRoomReservation.api.reservation.repository;

import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import com.app.meetingRoomReservation.api.reservation.entity.TimeSlice;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                        reservation.meetingRoom.id.eq(meetingRoomId),
                        reservation.reservationStatusType.eq(ReservationStatusType.RESERVATION_CONFIRMATION)
                )
                .fetch();
    }

    private BooleanExpression getOtherUserConfirmReservationExpression(Long userId) {
        return reservation.userId.ne(userId)
                .and(reservation.reservationStatusType.eq(ReservationStatusType.RESERVATION_CONFIRMATION));
    }

    private BooleanExpression getMyReservationExpression(Long userId) {
        return reservation.userId.eq(userId);
    }

    private BooleanExpression getOverlapTimeExpression(TimeSlice timeSlice) {
        return reservation.timeSlice.timeEnd.gt(timeSlice.getTimeStart())
                .and(reservation.timeSlice.timeStart.lt(timeSlice.getTimeEnd()));
    }

}
