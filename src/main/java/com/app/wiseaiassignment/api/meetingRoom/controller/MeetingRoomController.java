package com.app.wiseaiassignment.api.meetingRoom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting-room")
public class MeetingRoomController {

    // 회의실 미팅 조회
    @GetMapping
    public ResponseEntity<List> getMeetingRooms() {
        return ResponseEntity.ok(List.of());
    }

}
