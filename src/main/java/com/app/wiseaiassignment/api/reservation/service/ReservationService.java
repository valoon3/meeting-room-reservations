package com.app.wiseaiassignment.api.reservation.service;

import com.app.wiseaiassignment.api.meetingRoom.entity.MeetingRoom;
import com.app.wiseaiassignment.api.meetingRoom.repository.MeetingRoomRepository;
import com.app.wiseaiassignment.api.reservation.dto.CreateReservationRequest;
import com.app.wiseaiassignment.api.reservation.entity.Reservation;
import com.app.wiseaiassignment.api.reservation.entity.TimeSlice;
import com.app.wiseaiassignment.api.reservation.repository.ReservationCustomRepository;
import com.app.wiseaiassignment.api.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationCustomRepository reservationCustomRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    @Transactional
    public Long createReservation(Long meetingRoomId, CreateReservationRequest request) {

        TimeSlice reservationTimeSlice = TimeSlice.create(request.getStartTime(), request.getEndTime());
        validAlreadyReservationRoom(meetingRoomId, request, reservationTimeSlice);

        // todo: 미팅룸 예외처리
        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                .orElseThrow(() -> new RuntimeException("미팅룸을 찾을 수 없습니다."));
        int totalPrice = getTotalPrice(reservationTimeSlice, meetingRoom);

        Reservation reservation = Reservation.create(
                request.getUserId(),
                totalPrice,
                reservationTimeSlice,
                meetingRoom
        );
        return reservationRepository.save(reservation).getId();
    }

    private void validAlreadyReservationRoom(Long meetingRoomId, CreateReservationRequest request, TimeSlice reservationTimeSlice) {
        List<Reservation> alreadyReservationMeetingRoom = reservationCustomRepository.getAlreadyReservationMeetingRoom(meetingRoomId, request.getUserId(), reservationTimeSlice);

        if(!alreadyReservationMeetingRoom.isEmpty()) {
            // todo: 이미 예약된 시간에 예약을 시도한 경우 예외 처리
        }
    }

    private int getTotalPrice(TimeSlice reservationTimeSlice, MeetingRoom meetingRoom) {
        return reservationTimeSlice.getCalculateDurationUnits() * meetingRoom.getHourlyPrice() / 2;
    }

}
