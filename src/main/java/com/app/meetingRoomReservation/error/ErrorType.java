package com.app.meetingRoomReservation.error;

import lombok.Getter;

@Getter
public enum ErrorType {

    // 테스트 용도
    TEST("001", "badrequest exception test"),

    // timeSlice error
    INCORRECT_TIME_ORDER_REQUEST("TS-01", "종료 시간은 시작 시간보다 이후여야 합니다."),
    BAD_TIME_UNIT_REQUEST("TS-02", "예약 시간은 정시 또는 30분 단위로만 가능합니다.");

    // 에러 코드 (예: "A-001", "M-002" 등)
    private String errorCode;

    // 에러 메시지 (예: "토큰이 만료되었습니다.", "이미 가입된 회원입니다." 등)
    private String errorMessage;

    ErrorType(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
