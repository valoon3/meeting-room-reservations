package com.app.meetingRoomReservation.error.exceptions;

import com.app.meetingRoomReservation.error.ErrorType;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private ErrorType errorType;

    public BadRequestException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }
}
