package com.nisum.challenge.usersms.controller;

import com.nisum.challenge.usersms.domain.AuthRequest;
import com.nisum.challenge.usersms.domain.AuthResponse;
import com.nisum.challenge.usersms.domain.CreateUserRequest;
import com.nisum.challenge.usersms.domain.UserResponse;
import com.nisum.challenge.usersms.exceptions.ForbiddenUserException;
import com.nisum.challenge.usersms.exceptions.UnauthorizedUserException;
import com.nisum.challenge.usersms.exceptions.ValidationException;
import com.nisum.challenge.usersms.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/oauth/token")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse authenticateAndGetToken(@RequestBody final AuthRequest authRequest)
            throws UnauthorizedUserException, ForbiddenUserException {

        return usersService.authenticateAndGetToken(authRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody final CreateUserRequest createUserRequest) throws ValidationException {

        return usersService.createUser(createUserRequest);
    }
}
