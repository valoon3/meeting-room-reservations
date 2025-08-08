package com.app.meetingRoomReservation.api.meetingRoom.repository;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
