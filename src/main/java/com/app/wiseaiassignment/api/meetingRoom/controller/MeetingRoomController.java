package com.app.wiseaiassignment.api.meetingRoom.controller;

import com.app.wiseaiassignment.api.meetingRoom.dto.MeetingRoomResponse;
import com.app.wiseaiassignment.api.meetingRoom.service.MeetingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting-rooms")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    // 회의실 목록 조회
    @GetMapping
    public ResponseEntity<List<MeetingRoomResponse>> getMeetingRooms() {
        return ResponseEntity.ok(meetingRoomService.getMeetingRooms());
    }

}
