package com.gamelink.gamelinkapi.repositories;

import com.gamelink.gamelinkapi.models.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(@NotBlank String username);
}