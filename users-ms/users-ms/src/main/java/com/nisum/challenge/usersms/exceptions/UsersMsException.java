package com.nisum.challenge.usersms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UsersMsException extends Exception {
    private final String message;

    public UsersMsException(final String message) {
        this.message = message;
    }
}
