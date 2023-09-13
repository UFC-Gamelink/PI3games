package com.gamelink.gamelinkapi.repositories;

import com.gamelink.gamelinkapi.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
