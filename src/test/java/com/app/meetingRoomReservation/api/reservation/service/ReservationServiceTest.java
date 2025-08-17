package com.app.meetingRoomReservation.api.reservation.service;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.meetingRoom.repository.MeetingRoomRepository;
import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.constant.ProviderType;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.payment.repository.PaymentRepository;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.paymentProvider.repository.PaymentProviderRepository;
import com.app.meetingRoomReservation.api.reservation.dto.CreateReservationRequest;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import com.app.meetingRoomReservation.api.reservation.repository.ReservationCustomRepository;
import com.app.meetingRoomReservation.api.reservation.repository.ReservationRepository;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ReservationService 통합 테스트 - 지정 시나리오")
class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentProviderRepository paymentProviderRepository;

    private MeetingRoom testMeetingRoom;
    private PaymentProvider testPaymentProvider;
    @Autowired
    private ReservationCustomRepository reservationCustomRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 미팅룸 생성
        testMeetingRoom = MeetingRoom.create("테스트 회의실", 10, 20000);
        testMeetingRoom = meetingRoomRepository.save(testMeetingRoom);

        // 테스트용 결제 제공자 생성
        testPaymentProvider = PaymentProvider.create(ProviderType.A_PAY, "", "");
        testPaymentProvider = paymentProviderRepository.save(testPaymentProvider);
    }

    @AfterEach
    void tearDown() {
        meetingRoomRepository.delete(testMeetingRoom);
        paymentProviderRepository.delete(testPaymentProvider);
    }

    @Test
    @DisplayName("시나리오 1: 정상적으로 성공하는 케이스")
    void scenario1_정상적인_예약_생성_성공() {
        // given
        Long userId = 1L;
        LocalDateTime startTime = LocalDateTime.of(2025, 8, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 8, 15, 11, 0);
        CreateReservationRequest request = new CreateReservationRequest(userId, startTime, endTime);

        // when
        Long reservationId = reservationService.createReservation(testMeetingRoom.getId(), request);

        List<Reservation> reservationWithPayment = reservationCustomRepository.getReservationWithPayment(reservationId);

        // then
        assertNotNull(reservationId, "예약 ID가 생성되어야 합니다.");
        assertEquals(2, reservationWithPayment.size(), "2개의 예약이 생성되어야 합니다.");
        Long paymentId = reservationWithPayment.get(0).getPayment().getId();
        reservationWithPayment.forEach(reservation ->
                assertEquals(paymentId, reservation.getPayment().getId(), "모든 예약의 payment ID가 동일해야 합니다.")
        );

        reservationRepository.deleteAll(reservationWithPayment);
        paymentRepository.delete(reservationWithPayment.get(0).getPayment());
    }

    @Test
    @DisplayName("시나리오 2: 동시에 10:00~11:00 예약, 10:30~11:30 예약 두 건이 겹쳤을 때의 에러 확인")
    void scenario2_겹치는_시간대_동시_예약_시_에러_발생() throws InterruptedException {
        // given
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        AtomicLong successfulReservationId = new AtomicLong(0);

        Long meetingRoomId = testMeetingRoom.getId();

        // 첫 번째 예약: 10:00 ~ 11:00
        CreateReservationRequest request1 = new CreateReservationRequest(
                1L,
                LocalDateTime.of(2025, 8, 15, 10, 0),
                LocalDateTime.of(2025, 8, 15, 11, 0)
        );

        // 두 번째 예약: 10:30 ~ 11:30 (30분 겹침)
        CreateReservationRequest request2 = new CreateReservationRequest(
                2L,
                LocalDateTime.of(2025, 8, 15, 10, 30),
                LocalDateTime.of(2025, 8, 15, 11, 30)
        );

        CreateReservationRequest[] requests = {request1, request2};

        // when - 동시에 두 예약 요청 실행
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            executorService.submit(() -> {
                try {
                    Long reservationId = reservationService.createReservation(meetingRoomId, requests[threadIndex]);
                    List<Reservation> reservationWithPayment = reservationCustomRepository.getReservationWithPayment(reservationId);
                    reservationRepository.deleteAll(reservationWithPayment);
                    paymentRepository.delete(reservationWithPayment.get(0).getPayment());
                    successCount.incrementAndGet();
                    successfulReservationId.set(reservationId);
                    System.out.println("성공한 예약 - Thread " + threadIndex + ", 예약 ID: " + reservationId);
                } catch (DataIntegrityViolationException e) {
                    failCount.incrementAndGet();
                    System.out.println("예상된 실패 - Thread " + threadIndex + ", 에러: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("예상하지 못한 예외 - Thread " + threadIndex + ", 에러: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        assertEquals(1, successCount.get(), "겹치는 시간대의 예약 중 하나만 성공해야 합니다.");
        assertEquals(1, failCount.get(), "나머지 하나는 ALREADY_RESERVATION_MEETING_ROOM 에러로 실패해야 합니다.");
        assertTrue(successfulReservationId.get() > 0, "성공한 예약 ID가 설정되어야 합니다.");
    }

    @Test
    @Transactional
    @DisplayName("시나리오 3: 정상적으로 종료되었을 때 Payment가 생성되어 있어야 한다")
    void scenario3_정상_종료_시_Payment_생성_확인() {
        // given
        Long userId = 3L;
        LocalDateTime startTime = LocalDateTime.of(2025, 8, 15, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 8, 15, 15, 30); // 1시간 30분
        CreateReservationRequest request = new CreateReservationRequest(userId, startTime, endTime);

        // when
        Long reservationId = reservationService.createReservation(testMeetingRoom.getId(), request);

        // then
        assertNotNull(reservationId, "예약 ID가 생성되어야 합니다.");

        // Payment 생성 확인
        List<Payment> savedPayments = paymentRepository.findAll();
        assertEquals(1, savedPayments.size(), "Payment가 1개 생성되어야 합니다.");

        Payment savedPayment = savedPayments.get(0);

        // Payment 기본 정보 검증
        assertNotNull(savedPayment.getId(), "Payment ID가 생성되어야 합니다.");
        assertEquals(PaymentStatusType.PENDING, savedPayment.getPaymentStatusType(),
                "Payment 상태가 PENDING이어야 합니다.");

        // Payment 금액 검증 (1시간 30분 = 3개 슬롯 * 20000 / 2 = 30000)
        assertEquals(30000, savedPayment.getTotalPrice(),
                "Payment 총 금액이 올바르게 계산되어야 합니다.");

        // Payment와 Reservation 연관관계 검증
        assertEquals(3, savedPayment.getReservations().size(),
                "Payment에 3개의 예약이 연결되어야 합니다.");

        // 각 예약이 올바른 Payment를 참조하는지 검증
        savedPayment.getReservations().forEach(reservation -> {
            assertEquals(savedPayment.getId(), reservation.getPayment().getId(),
                    "각 예약이 올바른 Payment를 참조해야 합니다.");
            assertEquals(userId, reservation.getUserId(),
                    "각 예약의 사용자 ID가 일치해야 합니다.");
        });

        // 데이터베이스의 전체 예약 수 확인
        List<Reservation> allReservations = reservationRepository.findAll();
        assertEquals(3, allReservations.size(), "총 3개의 예약이 생성되어야 합니다.");

        // 각 예약이 Payment와 연결되었는지 확인
        allReservations.forEach(reservation -> {
            assertNotNull(reservation.getPayment(), "각 예약이 Payment와 연결되어야 합니다.");
            assertEquals(savedPayment.getId(), reservation.getPayment().getId(),
                    "각 예약이 올바른 Payment와 연결되어야 합니다.");
        });

        // 시간 슬롯 검증
        List<LocalDateTime> startTimes = allReservations.stream()
                .map(r -> r.getTimeSlice().getTimeStart())
                .sorted()
                .toList();

        assertEquals(LocalDateTime.of(2025, 8, 15, 14, 0), startTimes.get(0),
                "첫 번째 슬롯이 올바르게 설정되어야 합니다.");
        assertEquals(LocalDateTime.of(2025, 8, 15, 14, 30), startTimes.get(1),
                "두 번째 슬롯이 올바르게 설정되어야 합니다.");
        assertEquals(LocalDateTime.of(2025, 8, 15, 15, 0), startTimes.get(2),
                "세 번째 슬롯이 올바르게 설정되어야 합니다.");

        System.out.println("생성된 Payment ID: " + savedPayment.getId());
        System.out.println("Payment 총 금액: " + savedPayment.getTotalPrice());
        System.out.println("연결된 예약 수: " + savedPayment.getReservations().size());

        List<Reservation> reservationWithPayment = reservationCustomRepository.getReservationWithPayment(reservationId);
        reservationRepository.deleteAll(reservationWithPayment);
        paymentRepository.delete(reservationWithPayment.get(0).getPayment());
    }
}