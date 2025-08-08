package com.app.wiseaiassignment.api.meetingRoom.repository;

import com.app.wiseaiassignment.api.meetingRoom.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
