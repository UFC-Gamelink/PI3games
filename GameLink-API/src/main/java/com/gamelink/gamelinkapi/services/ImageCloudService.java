package com.gamelink.gamelinkapi.services;

import com.gamelink.gamelinkapi.exceptions.SaveThreatementException;
import com.gamelink.gamelinkapi.models.ImageModel;
import com.gamelink.gamelinkapi.repositories.images.CloudinaryRepository;
import com.gamelink.gamelinkapi.repositories.images.ImageRepository;
import com.gamelink.gamelinkapi.utils.PropertiesCopierUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageCloudService {
    private final CloudinaryRepository cloudinaryRepository;
    private final ImageRepository imageRepository;

    public ImageModel saveImageOrThrowSaveThreatementException(MultipartFile file) {
        ImageModel imageModel;
        try {
            imageModel = cloudinaryRepository.saveImage(file);
            return imageRepository.save(imageModel);
        } catch (IOException e) {
            throw new SaveThreatementException("Error during saving images");
        }
    }

    public void deleteImageOrThrowSaveThreatementException(ImageModel imageModel) {
        try {
            cloudinaryRepository.deleteImage(imageModel.getPublicId());
            imageRepository.deleteById(imageModel.getId());
        } catch (Exception e) {
            throw new SaveThreatementException("Error during deleting the image");
        }
    }

    public ImageModel updateImageOrThrowSaveThreatementException(ImageModel imageModel, MultipartFile imageToBeSaved) {
        try {
            ImageModel imageUpdated = cloudinaryRepository.saveImage(imageToBeSaved);

            cloudinaryRepository.deleteImage(imageModel.getPublicId());
            ImageModel imageToBeUpdated = imageRepository.findById(imageModel.getId()).orElseThrow();
            PropertiesCopierUtils.copyNonNullProperties(imageUpdated, imageToBeUpdated);
            return imageRepository.save(imageToBeUpdated);
        } catch (Exception e) {
            throw new SaveThreatementException("Error during updating the images");
        }
    }
}
