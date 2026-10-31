package com.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AppUserReqDTO {
    @NotBlank
    private String name;
    @NotBlank
    @Size(min=10,max=20)
    private String password;
    @Email
    @NotBlank
    private String email;
}
