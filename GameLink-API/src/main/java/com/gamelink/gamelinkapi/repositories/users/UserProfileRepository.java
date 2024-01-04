package com.gamelink.gamelinkapi.repositories.users;

import com.gamelink.gamelinkapi.models.user.User;
import com.gamelink.gamelinkapi.models.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findUserProfileByUser(User user);
}
