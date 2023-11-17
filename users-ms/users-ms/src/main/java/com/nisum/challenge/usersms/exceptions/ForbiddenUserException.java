package com.nisum.challenge.usersms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenUserException extends UsersMsException {
    public ForbiddenUserException(final String user) {
        super(String.format("El usuario %s no tiene permisos", user));
    }
}
