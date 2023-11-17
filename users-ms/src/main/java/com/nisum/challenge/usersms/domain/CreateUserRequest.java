package com.nisum.challenge.usersms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String name;
//    @Email
    private String email;
//    @Password
    private String password;
    private List<Phone> phones;
}
