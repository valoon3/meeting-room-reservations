package com.app.meetingRoomReservation.api.payment.dto;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResultDto {

    private Long paymentId;
    private ProviderType providerType; 
    private String Status;


}
