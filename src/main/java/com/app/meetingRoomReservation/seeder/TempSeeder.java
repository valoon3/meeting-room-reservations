package com.app.meetingRoomReservation.seeder;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.meetingRoom.repository.MeetingRoomRepository;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.paymentProvider.repository.PaymentProviderRepository;
import com.app.meetingRoomReservation.api.user.entity.User;
import com.app.meetingRoomReservation.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TempSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final MeetingRoomRepository meetingRoomRepository;
    private final PaymentProviderRepository paymentProviderRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedUser();
        seedMeetingRoom();
        seedPaymentProvider();
    }

    private void seedUser() {
        User user1 = User.create("test1@test.com", "test", "test1", "010-1111-1111");
        User user2 = User.create("test2@test.com", "test", "test2", "010-2222-2222");
        User user3 = User.create("test3@test.com", "test", "test3", "010-3333-3333");

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    private void seedMeetingRoom() {
        MeetingRoom roomA = MeetingRoom.create("roomA", 2, 10000);
        MeetingRoom roomB = MeetingRoom.create("roomB", 4, 20000);
        MeetingRoom roomC = MeetingRoom.create("roomC", 6, 40000);
        MeetingRoom roomD = MeetingRoom.create("roomD", 9, 80000);

        meetingRoomRepository.saveAll(List.of(roomA, roomB, roomC, roomD));
    }

    private void seedPaymentProvider() {
        PaymentProvider secretA = PaymentProvider.create(ProviderType.A_PAY, "http://localhost:8081/api/v1/mock-payment-api/a-pay", "secretA");
        PaymentProvider secretB = PaymentProvider.create(ProviderType.B_PAY, "http://localhost:8081/api/v1/mock-payment-api/b-pay", "secretB");
        PaymentProvider secretC = PaymentProvider.create(ProviderType.C_PAY, "http://localhost:8081/api/v1/mock-payment-api/c-pay", "secretC");

        paymentProviderRepository.saveAll(List.of(secretA, secretB, secretC));
    }
}
