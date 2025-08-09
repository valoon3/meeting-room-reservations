package com.app.meetingRoomReservation.error.exceptions;

import com.app.meetingRoomReservation.error.ErrorType;
import lombok.Getter;

@Getter
public class ExternalServiceUnavailableException extends RuntimeException {

    private ErrorType errorType;

    public ExternalServiceUnavailableException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public ExternalServiceUnavailableException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
    }

    public ExternalServiceUnavailableException(ErrorType errorType, Throwable e) {
        super(e);
        this.errorType = errorType;
    }

}
