package com.app.meetingRoomReservation.api.paymentProvider.entity;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
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

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(nullable = false)
    private String apiUrl;

    private String apiSecret;

    public static PaymentProvider create(ProviderType providerName, String apiUrl, String apiSecret) {
        PaymentProvider paymentProvider = new PaymentProvider();
        paymentProvider.providerType = providerName;
        paymentProvider.apiUrl = apiUrl;
        paymentProvider.apiSecret = apiSecret;
        return paymentProvider;
    }
}
