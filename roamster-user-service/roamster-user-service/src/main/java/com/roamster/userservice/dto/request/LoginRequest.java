package com.roamster.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String login;   // accepts login OR email

    @NotBlank
    private String password;
}
