package com.gamelink.gamelinkapi.repositories.images;

import com.gamelink.gamelinkapi.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, UUID> {
    void deleteByPublicId(String publicId);
}
