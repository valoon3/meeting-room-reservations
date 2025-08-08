package com.app.meetingRoomReservation.api.reservation.service;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.meetingRoom.repository.MeetingRoomRepository;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.payment.repository.PaymentRepository;
import com.app.meetingRoomReservation.api.reservation.dto.ConfirmReservationResponse;
import com.app.meetingRoomReservation.api.reservation.dto.CreateReservationRequest;
import com.app.meetingRoomReservation.api.reservation.dto.PaymentRequest;
import com.app.meetingRoomReservation.api.reservation.entity.Reservation;
import com.app.meetingRoomReservation.api.reservation.entity.TimeSlice;
import com.app.meetingRoomReservation.api.reservation.repository.ReservationCustomRepository;
import com.app.meetingRoomReservation.api.reservation.repository.ReservationRepository;
import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import com.app.meetingRoomReservation.error.exceptions.EntityNotFoundException;
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
    private final PaymentRepository paymentRepository;

    @Transactional
    public Long createReservation(Long meetingRoomId, CreateReservationRequest request) {

        TimeSlice reservationTimeSlice = TimeSlice.create(request.getStartTime(), request.getEndTime());
        validAlreadyReservationRoom(meetingRoomId, request, reservationTimeSlice);

        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorType.MEETING_ROOM_NOT_FOUND));
        int totalPrice = getTotalPrice(reservationTimeSlice, meetingRoom);

        Reservation reservation = Reservation.create(
                request.getUserId(),
                totalPrice,
                reservationTimeSlice,
                meetingRoom
        );
        return reservationRepository.save(reservation).getId();
    }

    public List<ConfirmReservationResponse> selectConfirmReservations(Long meetingRoomId) {
        List<Reservation> reservations = reservationCustomRepository.getConfirmReservationMeetingRoom(meetingRoomId);

        return reservations.stream()
                .map(reservation -> new ConfirmReservationResponse(
                        reservation.getId(),
                        reservation.getMeetingRoom().getName(),
                        reservation.getMeetingRoom().getCapacity(),
                        reservation.getTimeSlice().getTimeStart(),
                        reservation.getTimeSlice().getTimeEnd()
                ))
                .toList();
    }

    private void validAlreadyReservationRoom(Long meetingRoomId, CreateReservationRequest request, TimeSlice reservationTimeSlice) {
        List<Reservation> alreadyReservationMeetingRoom = reservationCustomRepository.getAlreadyReservationMeetingRoom(meetingRoomId, request.getUserId(), reservationTimeSlice);

        if(!alreadyReservationMeetingRoom.isEmpty()) {
            throw new BadRequestException(ErrorType.ALREADY_RESERVATION_MEETING_ROOM);
        }
    }

    private int getTotalPrice(TimeSlice reservationTimeSlice, MeetingRoom meetingRoom) {
        return reservationTimeSlice.getCalculateDurationUnits() * meetingRoom.getHourlyPrice() / 2;
    }

    @Transactional
    public void createPayment(Long reservationId, PaymentRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorType.RESERVATION_NOT_FOUND));

        Payment payment = Payment.create(request.getProviderType(), request.getPaymentAmount(), reservation);
        paymentRepository.save(payment);

        // todo: webhook mock server 완성 후 작성
    }
}
