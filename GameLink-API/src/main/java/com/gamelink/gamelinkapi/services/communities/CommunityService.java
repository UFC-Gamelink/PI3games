package com.gamelink.gamelinkapi.services.communities;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunitiesGeneralResponse;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunityResponse;
import com.gamelink.gamelinkapi.dtos.responses.communities.PostCommunityResponse;
import com.gamelink.gamelinkapi.dtos.responses.posts.PostResponse;
import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
import com.gamelink.gamelinkapi.mappers.CommunityMapper;
import com.gamelink.gamelinkapi.models.BaseModel;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.communities.CommunityRepository;
import com.gamelink.gamelinkapi.services.cloudinary.ImageCloudService;
import com.gamelink.gamelinkapi.services.posts.PostService;
import com.gamelink.gamelinkapi.services.users.UserService;
import com.gamelink.gamelinkapi.utils.PropertiesCopierUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final ImageCloudService imageCloudService;
    private final PostService postService;
    private static final CommunityMapper communityMapper = CommunityMapper.INSTANCE;

    public PostCommunityResponse createCommunity(CommunityRequest communityRequest) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityToBeSaved = communityMapper.requestToModel(communityRequest);
        communityToBeSaved.setOwner(user);

        UUID communityId = communityRepository.save(communityToBeSaved).getId();
        return new PostCommunityResponse(communityId);
    }

    public CommunityResponse createBanner(UUID id, MultipartFile banner) {
        CommunityModel communityFounded = findCommunityById(id);

        if (communityFounded.getBanner() == null) {
            communityFounded.setBanner(imageCloudService.saveImageOrThrowSaveThreatementException(banner));
            return communityMapper.modelToResponse(communityRepository.save(communityFounded));
        } else {
            throw new SaveThreatementException("Banner is already saved");
        }
    }

    @Transactional
    public CommunityResponse updateCommunity(UUID id, CommunityRequest communityRequest) {
        CommunityModel communityToBeUpdated = findCommunityById(id);
        PropertiesCopierUtils.copyNonNullProperties(communityMapper.requestToModel(communityRequest), communityToBeUpdated);

        return communityMapper.modelToResponse(
                communityRepository.save(communityToBeUpdated)
        );
    }

    @Transactional
    public CommunityResponse updateBanner(UUID id, MultipartFile banner) {
        CommunityModel communityFounded = findCommunityById(id);

        if (communityFounded.getBanner() != null) {
            communityFounded.setBanner(
                    imageCloudService.updateImageOrThrowSaveThreatementException(communityFounded.getBanner(),  banner)
            );
            return communityMapper.modelToResponse(communityRepository.save(communityFounded));
        } else {
            throw new SaveThreatementException("Banner is not saved yet");
        }
    }

    public List<CommunitiesGeneralResponse> getCommunities() {
        return communityRepository.findAll()
                .stream()
                .map(communityMapper::modelToGeneralResponse)
                .toList();
    }

    public CommunityResponse getCommunity(UUID communityId) {
        CommunityModel communityModel = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        return communityMapper.modelToResponse(communityModel);
    }

    public List<PostResponse> getCommunityPosts(UUID communityId) {
        CommunityModel communityModel = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Community not found"));

        return communityModel.getPosts().stream()
                .sorted(Comparator.comparing(BaseModel::getCreatedAt))
                .map(postService::prepareResponse)
                .toList();
    }

    public List<CommunitiesGeneralResponse> getMyCommunities() {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        return communityRepository.findMyCommunities(user)
                .stream()
                .map(communityMapper::modelToGeneralResponse)
                .toList();
    }

    @Transactional
    public void deleteCommunity(UUID id) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityFounded = findCommunityIfExistsOrElseThrowsEntityNotFoundException(id);

        if (communityFounded.getOwner().getId() == user.getId()) {
            communityRepository.deleteById(id);
            imageCloudService.deleteImageOrThrowSaveThreatementException(communityFounded.getBanner());
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }

    private CommunityModel findCommunityById(UUID id) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityFound = findCommunityIfExistsOrElseThrowsEntityNotFoundException(id);

        if (!communityFound.getOwner().equals(user)) {
            throw new BadCredentialsException("You can't update this community");
        }

        return communityFound;
    }

    private CommunityModel findCommunityIfExistsOrElseThrowsEntityNotFoundException(UUID id) {
        return communityRepository
                .findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Community not exists")
                );
    }

    @Transactional
    public void enterCommunity(UUID communityId) {
        User userFound = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityFound = findCommunityIfExistsOrElseThrowsEntityNotFoundException(communityId);

        communityFound.getMembers().add(userFound);
        communityRepository.save(communityFound);
    }

    @Transactional
    public void exitCommunity(UUID communityId) {
        User userFound = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityFound = findCommunityIfExistsOrElseThrowsEntityNotFoundException(communityId);

        communityFound.getMembers().remove(userFound);
        communityRepository.save(communityFound);
    }
}
