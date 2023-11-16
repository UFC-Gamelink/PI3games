package com.gamelink.gamelinkapi.services.cloudinary;

import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import com.gamelink.gamelinkapi.repositories.images.CloudinaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final CloudinaryRepository cloudinaryRepository;

    public ImageModel saveImageOrThrowSaveThreatementException(MultipartFile file) {
        ImageModel imageModel;
        try {
            imageModel = cloudinaryRepository.saveImage(file);
        } catch (IOException e) {
            throw new SaveThreatementException("Error during saving images");
        }
        return imageModel;
    }
    public void deleteImageOrThrowSaveThreatementException(String publicId) {
        try {
            cloudinaryRepository.deleteImage(publicId);
        } catch (Exception e) {
            throw new SaveThreatementException("Error during updating the images");
        }
    }
}
