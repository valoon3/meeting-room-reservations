package com.app.meetingRoomReservation.api.meetingRoom.controller;

import com.app.meetingRoomReservation.api.meetingRoom.dto.MeetingRoomResponse;
import com.app.meetingRoomReservation.api.meetingRoom.service.MeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "회의실 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meeting-rooms")
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    @Operation(summary = "회의실 목록 조회", description = "회의실 전체 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<MeetingRoomResponse>> getMeetingRooms() {
        return ResponseEntity.ok(meetingRoomService.getMeetingRooms());
    }

}
