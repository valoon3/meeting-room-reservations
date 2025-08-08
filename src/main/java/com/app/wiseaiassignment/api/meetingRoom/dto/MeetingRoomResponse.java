package com.app.wiseaiassignment.api.meetingRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingRoomResponse {
    private Long id;
    private String name;
    private int capacity;
    private int hourlyPrice;
}
