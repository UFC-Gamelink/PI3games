package com.gamelink.gamelinkapi.repositories.communities;

import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityModel, UUID> {
}
