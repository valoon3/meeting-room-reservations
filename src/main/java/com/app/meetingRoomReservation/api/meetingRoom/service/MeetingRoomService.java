package com.app.meetingRoomReservation.api.meetingRoom.service;

import com.app.meetingRoomReservation.api.meetingRoom.dto.MeetingRoomResponse;
import com.app.meetingRoomReservation.api.meetingRoom.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    public List<MeetingRoomResponse> getMeetingRooms() {
        return meetingRoomRepository.findAll()
                .stream()
                .map(meetingRoom -> new MeetingRoomResponse(
                        meetingRoom.getId(),
                        meetingRoom.getName(),
                        meetingRoom.getCapacity(),
                        meetingRoom.getHourlyPrice()
                ))
                .toList();
    }
}
