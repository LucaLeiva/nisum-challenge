package com.nisum.challenge.usersms.mapper;

import com.nisum.challenge.usersms.domain.CreateUserRequest;
import com.nisum.challenge.usersms.domain.UserResponse;
import com.nisum.challenge.usersms.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", imports = { ZonedDateTime.class })
public interface UsersMapper {

    @Mappings({
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "lastLogin", expression = "java(ZonedDateTime.now())"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "modified", ignore = true),
            @Mapping(target = "token", ignore = true)
    })
    UserEntity toEntity(CreateUserRequest createUserRequest);

    UserResponse toDto(UserEntity userEntity);
}
