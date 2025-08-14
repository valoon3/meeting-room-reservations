package com.app.meetingRoomReservation.api.reservation.service;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import com.app.meetingRoomReservation.api.meetingRoom.repository.MeetingRoomRepository;
import com.app.meetingRoomReservation.api.payment.constant.PaymentStatusType;
import com.app.meetingRoomReservation.api.payment.entity.Payment;
import com.app.meetingRoomReservation.api.payment.gateway.PaymentGatewayFactory;
import com.app.meetingRoomReservation.api.payment.repository.PaymentRepository;
import com.app.meetingRoomReservation.api.paymentProvider.entity.PaymentProvider;
import com.app.meetingRoomReservation.api.paymentProvider.repository.PaymentProviderCustomRepository;
import com.app.meetingRoomReservation.api.reservation.constant.ReservationStatusType;
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
    private final PaymentGatewayFactory paymentGatewayFactory;
    private final PaymentProviderCustomRepository paymentProviderCustomRepository;

    @Transactional
    public Long createReservation(Long meetingRoomId, CreateReservationRequest request) {

        List<TimeSlice> reservationTimeSlices = TimeSlice.createList(request.getStartTime(), request.getEndTime());

        MeetingRoom meetingRoom = meetingRoomRepository.findById(meetingRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorType.MEETING_ROOM_NOT_FOUND));


        List<Reservation> reservationList = reservationTimeSlices.stream()
                .map(timeSlice -> Reservation.create(request.getUserId(), timeSlice, meetingRoom))
                .toList();
        reservationRepository.saveAll(reservationList);

        int totalPrice = getTotalPrice(reservationTimeSlices, meetingRoom);
        Payment payment = Payment.create(totalPrice, reservationList);
        paymentRepository.save(payment);


        return reservationList.get(0).getId();
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

    @Transactional
    public void processPayment(Long reservationId, PaymentRequest request) {
        List<Reservation> reservation = reservationCustomRepository.getReservationWithPayment(reservationId);
        if(reservation.isEmpty()) {
            throw new EntityNotFoundException(ErrorType.RESERVATION_NOT_FOUND);
        }
        reservation.forEach(r -> {
            r.updateReservationStatus(ReservationStatusType.PAYMENT_PROGRESS);
        });

        PaymentProvider paymentProvider = paymentProviderCustomRepository.findByProviderType(request.getProviderType());
        Payment payment = reservation.getFirst().getPayment();
        payment.updatePaymentStatus(PaymentStatusType.PENDING);
        payment.updatePaymentProvider(paymentProvider);

        var gateway = paymentGatewayFactory.getGateway(request.getProviderType());
        gateway.requestPayment(payment);
    }

    // todo: entity 변환 후 삭제
    private void validAlreadyReservationRoom(Long meetingRoomId, CreateReservationRequest request, TimeSlice reservationTimeSlice) {
        List<Reservation> alreadyReservationMeetingRoom = reservationCustomRepository.getAlreadyReservationMeetingRoom(meetingRoomId, request.getUserId(), reservationTimeSlice);

        if (!alreadyReservationMeetingRoom.isEmpty()) {
            throw new BadRequestException(ErrorType.ALREADY_RESERVATION_MEETING_ROOM);
        }
    }

    private int getTotalPrice(List<TimeSlice> reservationTimeSlice, MeetingRoom meetingRoom) {
        return reservationTimeSlice.size() * meetingRoom.getHourlyPrice() / 2;
    }
}
