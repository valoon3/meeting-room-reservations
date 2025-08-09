package com.app.meetingRoomReservation.api.payment.constant;

public enum ProviderType {
    A_PAY("a-pay"),
    B_PAY("b-pay"),
    C_PAY("c-pay")

    ;

    private final String type;

    ProviderType(String type) {
        this.type = type;
    }

}
