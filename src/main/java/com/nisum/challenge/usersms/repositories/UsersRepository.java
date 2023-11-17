package com.nisum.challenge.usersms.repositories;

import com.nisum.challenge.usersms.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String username);
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
