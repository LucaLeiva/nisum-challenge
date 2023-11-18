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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private UsersMapper usersMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AppConfig appConfig;

    @InjectMocks
    private UsersService service;

    @Test
    void test_when_createUser_expects_results() throws ValidationException {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("admin@hotmail.cl");
        createUserRequest.setPassword("adminadmin");
        UserEntity userEntity = new UserEntity();
        UserResponse userResponse = new UserResponse();

        when(usersRepository.existsByEmail(anyString())).thenReturn(false);
        when(appConfig.getValidateEmailExp()).thenReturn("^[a-zA-Z]+@[a-zA-Z]+\\.(cl)$");
        when(appConfig.getValidatePasswordExp()).thenReturn("^[a-zA-Z0-9]{8,22}$");
        when(usersMapper.toEntity(any(CreateUserRequest.class))).thenReturn(userEntity);
        when(usersRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(usersMapper.toDto(any(UserEntity.class))).thenReturn(userResponse);

        UserResponse response = service.createUser(createUserRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(userResponse, response);

        verify(usersRepository, times(1)).existsByEmail("admin@hotmail.cl");
        verify(appConfig, times(1)).getValidateEmailExp();
        verify(appConfig, times(1)).getValidatePasswordExp();
        verify(usersMapper, times(1)).toEntity(createUserRequest);
        verify(usersRepository, times(1)).save(userEntity);
        verify(usersMapper, times(1)).toDto(userEntity);
    }

    @Test
    void test_when_createUser_expects_ValidationException() throws ValidationException {
        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setEmail("adminsinarrobahotmail.cl");
        when(usersRepository.existsByEmail(anyString())).thenReturn(true);

        Assertions.assertThrows(ValidationException.class, () -> service.createUser(createUserRequest));

        when(usersRepository.existsByEmail(anyString())).thenReturn(false);
        when(appConfig.getValidateEmailExp()).thenReturn("^[a-zA-Z]+@[a-zA-Z]+\\.(cl)$");

        Assertions.assertThrows(ValidationException.class, () -> service.createUser(createUserRequest));

        createUserRequest.setEmail("admin@hotmail.cl");
        createUserRequest.setPassword("a");
        when(appConfig.getValidatePasswordExp()).thenReturn("^[a-zA-Z0-9]{8,22}$");
        Assertions.assertThrows(ValidationException.class, () -> service.createUser(createUserRequest));

        verify(usersRepository, times(3)).existsByEmail(anyString());
        verify(appConfig, times(2)).getValidateEmailExp();
        verify(appConfig, times(1)).getValidatePasswordExp();
        verify(usersMapper, never()).toEntity(any());
        verify(usersRepository, never()).save(any());
        verify(usersMapper, never()).toDto(any());
    }

    @Test
    void test_when_authenticateAndGetToken_expects_results() throws UnauthorizedUserException, ForbiddenUserException {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("admin@hotmail.com");
        authRequest.setPassword("adminadmin");
        UserEntity userEntity = new UserEntity();
        userEntity.setIsActive(true);

        Authentication authenticationMock = Mockito.mock(Authentication.class);

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        when(authenticationMock.isAuthenticated()).thenReturn(true);
        when(jwtUtils.generateToken(anyString())).thenReturn("sadasdasdasd");

        AuthResponse response = service.authenticateAndGetToken(authRequest);

        Assertions.assertNotNull(response);
        Assertions.assertAll(
                () -> Assertions.assertEquals("sadasdasdasd", response.getToken()),
                () -> Assertions.assertEquals("admin@hotmail.com", authRequest.getEmail())
        );

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        verify(authenticationMock, times(1)).isAuthenticated();
        verify(usersRepository, times(1)).findByEmail("admin@hotmail.com");
        verify(jwtUtils, times(1)).generateToken("admin@hotmail.com");
        verify(usersRepository, times(1)).save(userEntity);
    }

    @Test
    void test_when_authenticateAndGetToken_expects_UnauthorizedUserException()
            throws UnauthorizedUserException, ForbiddenUserException {

        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("admin@hotmail.com");
        authRequest.setPassword("adminadmin");

        Authentication authenticationMock = Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        when(authenticationMock.isAuthenticated()).thenReturn(false);

        Assertions.assertThrows(UnauthorizedUserException.class, () -> service.authenticateAndGetToken(authRequest));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        Assertions.assertThrows(UnauthorizedUserException.class, () -> service.authenticateAndGetToken(authRequest));

        verify(authenticationManager, times(2)).authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        verify(authenticationMock, times(1)).isAuthenticated();
        verify(usersRepository, never()).findByEmail(anyString());
        verify(jwtUtils, never()).generateToken(anyString());
        verify(usersRepository, never()).save(any());
    }
}
