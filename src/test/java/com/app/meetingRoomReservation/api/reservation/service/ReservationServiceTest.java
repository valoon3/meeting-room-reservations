package com.app.meetingRoomReservation.api.reservation.service;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.meetingRoom.repository.MeetingRoomRepository;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.payment.gateway.PaymentGateway;
import com.app.meetingRoomReservation.api.payment.gateway.PaymentGatewayFactory;
import com.app.meetingRoomReservation.api.payment.repository.PaymentRepository;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.paymentProvider.repository.PaymentProviderCustomRepository;
import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
import com.app.meetingRoomReservation.api.reservation.dto.CreateReservationRequest;
import com.app.meetingRoomReservation.api.reservation.dto.PaymentRequest;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import com.app.meetingRoomReservation.api.reservation.repository.ReservationCustomRepository;
import com.app.meetingRoomReservation.api.reservation.repository.ReservationRepository;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 순수 단위 테스트 환경 설정
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationCustomRepository reservationCustomRepository;
    @Mock private MeetingRoomRepository meetingRoomRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentGatewayFactory paymentGatewayFactory;
    @Mock private PaymentProviderCustomRepository paymentProviderCustomRepository;

    // 단위 테스트에서는 실제 DB를 사용하지 않으므로 @BeforeEach 제거

    @Test
    @DisplayName("예약 생성 - 성공")
    void createReservation_Success() {
        // given
        Long meetingRoomId = 1L;
        CreateReservationRequest request = new CreateReservationRequest(
                1L,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0)
        );
        MeetingRoom mockMeetingRoom = MeetingRoom.create("Test Room", 4, 10000);

        // <<< 해결책 1: NullPointerException 방지 >>>
        // save 메서드가 null을 반환하지 않도록, 받은 인자를 그대로 반환하도록 설정
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(meetingRoomRepository.findWithLockById(meetingRoomId)).thenReturn(Optional.of(mockMeetingRoom));
        when(reservationCustomRepository.getAlreadyReservationMeetingRoom(any(), any(), any())).thenReturn(Collections.emptyList());

        // when
        reservationService.createReservation(meetingRoomId, request);

        // then
        verify(meetingRoomRepository, times(1)).findWithLockById(meetingRoomId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 생성 - 실패 (이미 예약된 회의실)")
    void createReservation_Fail_AlreadyReserved() {
        // given
        Long meetingRoomId = 1L;
        CreateReservationRequest request = new CreateReservationRequest(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        MeetingRoom mockMeetingRoom = MeetingRoom.create("Test Room", 4, 10000);

        // when & then
        assertThrows(BadRequestException.class, () -> reservationService.createReservation(meetingRoomId, request));
    }

    @Test
    @DisplayName("결제 생성 - 성공")
    void processPayment_Success() {
        // given
        Long reservationId = 1L;
        int expectedPrice = 10000;
        PaymentRequest request = new PaymentRequest(ProviderType.A_PAY, expectedPrice);

        Reservation mockReservation = mock(Reservation.class);
        MeetingRoom mockMeetingRoom = mock(MeetingRoom.class);
        PaymentProvider mockPaymentProvider = mock(PaymentProvider.class);
        PaymentGateway mockGateway = mock(PaymentGateway.class);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
//        when(mockReservation.getTotalPrice()).thenReturn(expectedPrice);
        when(mockReservation.getMeetingRoom()).thenReturn(mockMeetingRoom);
        when(mockMeetingRoom.getId()).thenReturn(1L);
        when(meetingRoomRepository.findWithLockById(1L)).thenReturn(Optional.of(mockMeetingRoom));
        when(paymentProviderCustomRepository.findByProviderType(request.getProviderType())).thenReturn(mockPaymentProvider);
        when(paymentGatewayFactory.getGateway(request.getProviderType())).thenReturn(mockGateway);

        // when
        reservationService.processPayment(reservationId, request);

        // then
        verify(mockReservation, times(1)).updateReservationStatus(ReservationStatusType.PAYMENT_PROGRESS);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(mockGateway, times(1)).requestPayment(any(Payment.class));
    }
}