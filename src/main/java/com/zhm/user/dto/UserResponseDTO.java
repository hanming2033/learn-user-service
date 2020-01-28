package com.zhm.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserResponseDTO {
    @NotNull(message = "firstName cannot be null")
    private String givenName;

    @NotNull(message = "lastName cannot be null")
    private String familyName;

    @NotNull(message = "email cannot be null")
    @Email(message = "email format is invalid")
    private String email;

    @NotNull(message = "userId cannot be null")
    private String id;
}
