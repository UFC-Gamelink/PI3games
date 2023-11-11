package com.gamelink.gamelinkapi.services.community;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunityResponse;
import com.gamelink.gamelinkapi.mappers.CommunityMapper;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.posts.PostModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.communities.CommunityRepository;
import com.gamelink.gamelinkapi.services.users.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final CommunityMapper communityMapper = CommunityMapper.INSTANCE;

    public void createCommunity(CommunityRequest communityRequest) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityToBeSaved = communityMapper.requestToModel(communityRequest);
        communityToBeSaved.setOwner(user);

        communityRepository.save(communityToBeSaved);
    }

    public List<CommunityResponse> getCommunities() {
        return communityRepository.findAll()
                .stream()
                .map(communityMapper::modelToResponse)
                .toList();
    }

    public void deleteCommunity(UUID id) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityFounded = communityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("this community doesn't exists")
        );

        if (communityFounded.getOwner().getId() == user.getId()) {
            communityRepository.deleteById(id);
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }
}
