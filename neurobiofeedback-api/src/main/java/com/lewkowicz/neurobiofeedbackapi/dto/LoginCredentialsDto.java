package com.lewkowicz.neurobiofeedbackapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginCredentialsDto {

    @NotEmpty(message = "validation.emailNotEmpty")
    @Email(message = "validation.emailInvalid")
    String email;

    @NotEmpty(message = "validation.passwordNotEmpty")
    String password;

}
