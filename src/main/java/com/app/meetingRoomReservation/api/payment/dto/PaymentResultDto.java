package com.app.meetingRoomReservation.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResultDto {

    private Long paymentId;
    private String Status;


}
