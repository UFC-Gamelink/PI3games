package com.gamelink.gamelinkapi.repositories.communities;

import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityModel, UUID> {
    List<CommunityModel> findAllByMembersInOrOwner(Collection<List<User>> members, User owner);
}
