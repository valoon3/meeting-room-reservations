package com.app.meetingRoomReservation.api.reservation.service;

import com.app.meetingRoomReservation.api.reservation.dto.CreateReservationRequest;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("동시에 2개의 같은 시간 같은 회의실 예약 요청이 들어올 때, 단 1개의 요청만 성공해야 한다.")
    void testConcurrentReservationRequests() throws InterruptedException {
        // given
        int numberOfThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        Long meetingRoomId = 1L;
        // 주의: 매번 테스트 실행 시 다른 시간으로 변경해야 합니다.
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 3, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 3, 11, 0);

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            final long userId = i + 20; // 다른 테스트와 겹치지 않는 사용자 ID
            executorService.submit(() -> {
                try {
                    CreateReservationRequest request = new CreateReservationRequest(userId, startTime, endTime);
                    reservationService.createReservation(meetingRoomId, request);
                    successCount.incrementAndGet();
                } catch (BadRequestException e) {
                    if (ErrorType.ALREADY_RESERVATION_MEETING_ROOM == e.getErrorType()) {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        assertEquals(1, successCount.get(), "하나의 예약만 성공해야 합니다.");
        assertEquals(numberOfThreads - 1, failCount.get(), "나머지 예약은 실패해야 합니다.");
        System.out.println("Success Count: " + successCount.get());
        System.out.println("Fail Count: " + failCount.get());
    }

}
