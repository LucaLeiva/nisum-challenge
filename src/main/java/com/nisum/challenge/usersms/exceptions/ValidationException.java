package com.nisum.challenge.usersms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationException extends UsersMsException {
    public ValidationException(final String message) {
        super(message);
    }
}
