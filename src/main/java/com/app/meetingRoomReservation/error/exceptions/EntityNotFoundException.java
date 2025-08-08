package com.app.meetingRoomReservation.error.exceptions;

import com.app.meetingRoomReservation.error.ErrorType;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final ErrorType errorType;

    public EntityNotFoundException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }
}
