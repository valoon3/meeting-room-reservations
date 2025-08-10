package com.app.meetingRoomReservation.api.payment.dto;

import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SelectPaymentStatusResponse {

    @Schema(description = "결제 아이디", example = "1")
    private Long id;

    @Schema(description = "결제 제공자 타입", example = "a-pay")
    private ProviderType providerType;

    @Schema(description = "결제 금액", example = "10000")
    private int totalPrice;

    @Schema(description = "결제 상태", example = "SUCCESS")
    private PaymentStatusType paymentStatusType;
}
