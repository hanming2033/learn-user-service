package com.zhm.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequestDTO {
    @NotNull(message = "email cannot be null")
    @Email(message = "invalid email format")
    private String email;
    @NotNull
    private String password;
}
