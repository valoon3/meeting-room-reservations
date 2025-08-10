package com.app.meetingRoomReservation.api.meetingRoom.repository;

import com.app.meetingRoomReservation.api.meetingRoom.entity.MeetingRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MeetingRoom> findWithLockById(Long id);
}
