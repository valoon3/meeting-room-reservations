package com.app.wiseaiassignment.api.meetingRoom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingRoomResponse {
    @Schema(description = "미팅룸 아이디", example = "1")
    private Long id;

    @Schema(description = "미팅룸 이름", example = "roomA")
    private String name;

    @Schema(description = "미팅룸 설명", example = "10")
    private int capacity;

    @Schema(description = "미팅룸 한시간의 가격", example = "10000")
    private int hourlyPrice;
}
