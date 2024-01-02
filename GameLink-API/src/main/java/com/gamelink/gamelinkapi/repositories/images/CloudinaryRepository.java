package com.gamelink.gamelinkapi.repositories.images;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gamelink.gamelinkapi.models.images.ImageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CloudinaryRepository {
    private final Cloudinary cloudinary;
    public ImageModel saveImage(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            File fileConverted = convert(file);
            Map uploaded = cloudinary
                    .uploader()
                    .upload(fileConverted, ObjectUtils.emptyMap());

            if (fileConverted != null) fileConverted.delete();

            return ImageModel.builder()
                    .filename(file.getName())
                    .publicId((String) uploaded.get("public_id"))
                    .url((String) uploaded.get("url"))
                    .build();
        }
        throw new InvalidPropertiesFormatException("the input musts be a image");
    }

    public void deleteImage(String id) throws IOException {
        cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile file) {
        try {
            return writeFile(file);
        } catch (IOException e) {
            return null;
        }
    }

    private File writeFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new IOException();
        }

        return convFile;
    }
}
