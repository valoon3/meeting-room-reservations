package com.app.meetingRoomReservation.response;

import com.app.meetingRoomReservation.error.exceptions.BadRequestException;
import com.app.meetingRoomReservation.error.exceptions.EntityNotFoundException;
import com.app.meetingRoomReservation.error.exceptions.ExternalServiceUnavailableException;
import com.app.meetingRoomReservation.response.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException: {}", e.getErrorType().getErrorMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of("BadRequestException", e.getErrorType()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {

        log.error("EntityNotFoundException: {}", e.getErrorType().getErrorMessage(), e);
        return ResponseEntity
                .status(404)
                .body(ErrorResponse.of("EntityNotFoundException", e.getErrorType()));
    }

    @ExceptionHandler(ExternalServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceUnavailableException(ExternalServiceUnavailableException e) {
        log.error("ExternalServiceUnavailableException: {}", e.getErrorType().getErrorMessage(), e);
        return ResponseEntity
                .status(503)
                .body(ErrorResponse.of("ExternalServiceUnavailableException", e.getErrorType()));
    }

}
