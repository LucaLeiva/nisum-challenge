package com.nisum.challenge.usersms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedUserException extends UsersMsException {
    public UnauthorizedUserException() {
        super("El usuario no esta autenticado");
    }
}
