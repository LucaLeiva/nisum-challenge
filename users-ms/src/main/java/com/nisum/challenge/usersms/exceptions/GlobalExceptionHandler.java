package com.nisum.challenge.usersms.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({UsersMsException.class})
    private ResponseEntity<ErrorResponse> handlerUsersMsException(final UsersMsException e) {
        HttpStatus httpStatus = e.getClass().getAnnotation(ResponseStatus.class).value();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler({Exception.class})
    private ResponseEntity<ErrorResponse> handlerException(final Exception e) {
        log.error("Unknown error", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Se produjo un error desconocido")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
