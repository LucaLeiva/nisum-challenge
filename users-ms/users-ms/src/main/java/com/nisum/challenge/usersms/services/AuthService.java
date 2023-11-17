package com.nisum.challenge.usersms.services;

import com.nisum.challenge.usersms.repositories.UsersRepository;
import com.nisum.challenge.usersms.utils.UserInfoDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {
    private final UsersRepository usersRepository;

    // con este service obtengo los datos del usuario para el filtro de autenticacion, tal vez podria añadirse mas cosas

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username)
                .map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Usuario %s no encontrado", username)));
    }
}
