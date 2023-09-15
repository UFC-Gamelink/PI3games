package com.gamelink.gamelinkapi.repositories.users;

import com.gamelink.gamelinkapi.models.users.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
}
