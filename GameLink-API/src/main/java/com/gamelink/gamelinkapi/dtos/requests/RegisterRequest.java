package com.gamelink.gamelinkapi.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @Email String email,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$") String password
) {
}
