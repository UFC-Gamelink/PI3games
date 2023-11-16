package com.gamelink.gamelinkapi.services.communities;

import com.gamelink.gamelinkapi.dtos.requests.communities.CommunityRequest;
import com.gamelink.gamelinkapi.dtos.responses.communities.CommunityResponse;
import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
import com.gamelink.gamelinkapi.mappers.CommunityMapper;
import com.gamelink.gamelinkapi.models.comunities.CommunityModel;
import com.gamelink.gamelinkapi.models.users.User;
import com.gamelink.gamelinkapi.repositories.communities.CommunityRepository;
import com.gamelink.gamelinkapi.services.cloudinary.CloudinaryService;
import com.gamelink.gamelinkapi.services.users.UserService;
import com.gamelink.gamelinkapi.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final CommunityMapper communityMapper = CommunityMapper.INSTANCE;

    public void createCommunity(CommunityRequest communityRequest) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityToBeSaved = communityMapper.requestToModel(communityRequest);
        communityToBeSaved.setOwner(user);

        communityRepository.save(communityToBeSaved);
    }

    public CommunityResponse createBanner(UUID id, MultipartFile banner) {
        CommunityModel communityFounded = findCommunityById(id);

        if (communityFounded.getBanner() == null) {
            communityFounded.setBanner(cloudinaryService.saveImageOrThrowSaveThreatementException(banner));
            return communityMapper.modelToResponse(communityRepository.save(communityFounded));
        } else {
            throw new SaveThreatementException("Banner is already saved");
        }
    }

    @Transactional
    public CommunityResponse updateCommunity(UUID id, CommunityRequest communityRequest) {
        CommunityModel communityToBeUpdated = findCommunityById(id);
        Utils.copyNonNullProperties(communityMapper.requestToModel(communityRequest), communityToBeUpdated);

        return communityMapper.modelToResponse(
                communityRepository.save(communityToBeUpdated)
        );
    }

    @Transactional
    public CommunityResponse updateBanner(UUID id, MultipartFile banner) {
        CommunityModel communityFounded = findCommunityById(id);

        if (communityFounded.getBanner() != null) {
            cloudinaryService.deleteImageOrThrowSaveThreatementException(communityFounded.getBanner().getPublicId());
            communityFounded.setBanner(cloudinaryService.saveImageOrThrowSaveThreatementException(banner));
            return communityMapper.modelToResponse(communityRepository.save(communityFounded));
        } else {
            throw new SaveThreatementException("Banner is not saved yet");
        }
    }

    public List<CommunityResponse> getCommunities() {
        return communityRepository.findAll()
                .stream()
                .map(communityMapper::modelToResponse)
                .toList();
    }

    public void deleteCommunity(UUID id) {
        User user = userService.findUserAuthenticationContextOrThrowsBadCredentialException();
        CommunityModel communityFounded = findCommunityIfExistsOrElseThrowsEntityNotFoundException(id);

        if (communityFounded.getOwner().getId() == user.getId()) {
            communityRepository.deleteById(id);
        } else {
            throw new BadCredentialsException("Invalid user");
        }
    }

    private CommunityModel findCommunityById(UUID id){
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
}
