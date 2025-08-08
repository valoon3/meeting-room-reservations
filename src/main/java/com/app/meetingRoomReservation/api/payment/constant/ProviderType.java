package com.app.meetingRoomReservation.api.payment.constant;

public enum ProviderType {
    A_PAY("A-COMPANY"),
    B_PAY("B-COMPANY"),
    C_PAY("C-COMPANY")

    ;

    private final String type;

    ProviderType(String type) {
        this.type = type;
    }

}
