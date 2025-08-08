package com.app.meetingRoomReservation.api.reservation.constant;

import lombok.Getter;

@Getter
public enum ReservationStatusType {
    TEMPORARY_RESERVATION("임시예약"),
    PAYMENT_PROGRESS("결제진행"),
    RESERVATION_CONFIRMATION("예약확정")

    ;

    private final String type;

    ReservationStatusType(String type) {
        this.type = type;
    }
}
