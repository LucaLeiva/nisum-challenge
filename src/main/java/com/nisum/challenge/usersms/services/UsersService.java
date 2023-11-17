package com.nisum.challenge.usersms.services;

import com.nisum.challenge.usersms.configurations.AppConfig;
import com.nisum.challenge.usersms.domain.AuthRequest;
import com.nisum.challenge.usersms.domain.AuthResponse;
import com.nisum.challenge.usersms.domain.CreateUserRequest;
import com.nisum.challenge.usersms.domain.UserResponse;
import com.nisum.challenge.usersms.exceptions.ForbiddenUserException;
import com.nisum.challenge.usersms.exceptions.UnauthorizedUserException;
import com.nisum.challenge.usersms.exceptions.ValidationException;
import com.nisum.challenge.usersms.mapper.UsersMapper;
import com.nisum.challenge.usersms.models.UserEntity;
import com.nisum.challenge.usersms.repositories.UsersRepository;
import com.nisum.challenge.usersms.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AppConfig appConfig;

//    @Value("${com.nisum.challenge.usersms.validateEmailExp}")
//    private String validateEmailExp;
//    @Value("${com.nisum.challenge.usersms.validatePasswordExp}")
//    private String validatePasswordExp;

    // TODO tests unitarios
    // TODO readme y diagrama de la solucion
    // TODO Swagger

    public AuthResponse authenticateAndGetToken(final AuthRequest authRequest)
            throws UnauthorizedUserException, ForbiddenUserException {

        // TODO no me gusta este try, ver como se podria optimizar
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            if (!authentication.isAuthenticated()) {
                log.error("Invalid Credentilas");
                throw new UnauthorizedUserException();
            }
        } catch (AuthenticationException e) {
            log.error("Invalid credentials");
            throw new UnauthorizedUserException();
        }

        UserEntity user = usersRepository.findByEmail(authRequest.getEmail()).get();
        if (!user.getIsActive()) {
            throw new ForbiddenUserException(authRequest.getEmail());
        }

        user.setLastLogin(ZonedDateTime.now());
        usersRepository.save(user);

        return AuthResponse.builder()
                .token(jwtUtils.generateToken(authRequest.getEmail()))
                .name(authRequest.getEmail())
                .build();
    }

    public UserResponse createUser(final CreateUserRequest createUserRequest) throws ValidationException {
        validateCreateUserRequest(createUserRequest);

        UserEntity newUser = usersMapper.toEntity(createUserRequest);
        newUser.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        newUser.setToken(jwtUtils.generateToken(createUserRequest.getEmail()));

        return usersMapper.toDto(usersRepository.save(newUser));
    }

    // TODO mover las validaciones como annotations?
    private void validateCreateUserRequest(final CreateUserRequest createUserRequest) throws ValidationException {
        // en realidad creo que la mejor forma es acumular los errores en una lista y lanzarlos en una excepcion, ya
        // que puedo tener varios errores de validacion a la vez, pero no respetaria la interfaz
        // mensaje: mensaje de error

        if (usersRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new ValidationException("El correo ya esta registrado");
        }
        if (!createUserRequest.getEmail().matches(appConfig.getValidateEmailExp())) {
            throw new ValidationException("El correo esta mal formado");
        }
        if (!createUserRequest.getPassword().matches(appConfig.getValidatePasswordExp())) {
            throw new ValidationException("La contraseña esta mal formada");
        }
    }
}
