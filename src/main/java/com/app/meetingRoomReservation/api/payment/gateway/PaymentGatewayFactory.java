package com.app.meetingRoomReservation.api.payment.gateway;

import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentGatewayFactory {
    private final Map<ProviderType, PaymentGateway> gateways = new EnumMap<>(ProviderType.class);

    public PaymentGatewayFactory(List<PaymentGateway> gatewayList) {
        gatewayList.forEach(gateway -> gateways.put(gateway.getProviderType(), gateway));
    }

    public PaymentGateway getGateway(ProviderType providerType) {
        return gateways.get(providerType);
    }
}
