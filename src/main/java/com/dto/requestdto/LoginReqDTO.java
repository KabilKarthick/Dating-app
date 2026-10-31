package com.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReqDTO {

    @NotBlank(message = "Enter your email")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Enter your password")
    private String password;
}
