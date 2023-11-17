package com.nisum.challenge.usersms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private ZonedDateTime created;
    private ZonedDateTime modified;
    private ZonedDateTime lastLogin;
    private String token;
    private Boolean isActive;
}
