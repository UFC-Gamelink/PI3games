package com.gamelink.gamelinkapi.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthenticationRequest(
        @NotBlank String username,
        @Pattern(regexp = "(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") String password
) {
}
