package com.app.meetingRoomReservation.api.paymentProvider.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "payment_provider",
        indexes = {

        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String apiUrl;
    private String apiSecret;

    public static PaymentProvider create(String providerName, String apiUrl, String apiSecret) {
        PaymentProvider paymentProvider = new PaymentProvider();
        paymentProvider.name = providerName;
        paymentProvider.apiUrl = apiUrl;
        paymentProvider.apiSecret = apiSecret;
        return paymentProvider;
    }
}
