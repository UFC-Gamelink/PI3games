package com.gamelink.gamelinkapi.dtos.responses.authentication;

import java.util.UUID;

public record AuthenticationResponse(String token, UUID userId) {
}
