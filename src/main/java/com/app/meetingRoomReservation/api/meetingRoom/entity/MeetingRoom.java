package com.app.meetingRoomReservation.api.meetingRoom.entity;

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
            // todo: 시간당 금액은 짝수여야 합니다.
        }

        MeetingRoom meetingRoom = new MeetingRoom();
        meetingRoom.name = name;
        meetingRoom.capacity = capacity;
        meetingRoom.hourlyPrice = hourlyPrice;
        return meetingRoom;
    }
}
