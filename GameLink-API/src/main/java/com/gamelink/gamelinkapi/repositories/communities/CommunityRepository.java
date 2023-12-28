package com.gamelink.gamelinkapi.repositories.communities;

import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityModel, UUID> {
    @Query("SELECT mo FROM CommunityModel mo WHERE :user member OF mo.members OR mo.owner = :user")
    List<CommunityModel> findMyCommunities(User user);
}
