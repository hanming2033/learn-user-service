package com.zhm.user.mapper;

import com.zhm.user.domain.User;
import com.zhm.user.dto.UserCreateRequestDTO;
import com.zhm.user.dto.UserResponseDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected BCryptPasswordEncoder bCrypt;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encryptedPassword", ignore = true)
    //@Mapping(target = "encryptedPassword", expression = "java(bCrypt.encode(dto.getPassword()))")
    @Mapping(target = "lastName", source = "familyName")
    @Mapping(target = "firstName", source = "givenName")
    public abstract User userCreateRequestDTOToUser(UserCreateRequestDTO dto);

    @AfterMapping
    public void userCreateRequestDTOToUser(UserCreateRequestDTO dto, @MappingTarget User user) {
        user.setEncryptedPassword(bCrypt.encode(dto.getPassword()));
    }

    @Mapping(target = "givenName", source = "firstName")
    @Mapping(target = "familyName", source = "lastName")
    public abstract UserResponseDTO userToUserResponseDTO(User user);

    public String UUIDToString(UUID uuid) {
        return uuid.toString();
    }
}
