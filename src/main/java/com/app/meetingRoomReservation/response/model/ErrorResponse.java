package com.app.meetingRoomReservation.response.model;

import com.app.meetingRoomReservation.error.ErrorType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String error;       // 에러 코드 (예: "NOT_FOUND", "UNAUTHORIZED" 등)
    private String errorCode;   // ErrorType에 정의해둔 errorCode
    private String message;     // 에러 메시지
    private String status;      // error

    public static ErrorResponse of(String error, ErrorType errorType) {
        return ErrorResponse.builder()
                .error(error)
                .errorCode(errorType.getErrorCode())
                .message(errorType.getErrorMessage())
                .status("error")
                .build();
    }
}
