package com.app.meetingRoomReservation.api.meetingRoom.entity;

import com.app.meetingRoomReservation.error.ErrorType;
import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "meeting_room",
        indexes = {

        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int hourlyPrice;

    public static MeetingRoom create(String name, int capacity, int hourlyPrice) {
        if(hourlyPrice % 2 != 0) {
            throw new BadRequestException(ErrorType.INVALID_HOURLY_PRICE);
        }

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.name = name;
        meetingRoom.capacity = capacity;
        meetingRoom.hourlyPrice = hourlyPrice;
        return meetingRoom;
    }
}
