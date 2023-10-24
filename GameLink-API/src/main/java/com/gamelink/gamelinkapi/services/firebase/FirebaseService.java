package com.gamelink.gamelinkapi.services.firebase;

import com.google.api.core.ApiFuture;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseService {
    private static final String url = "user-profile-image";


    public ApiFuture saveImage(MultipartFile file) throws Exception {
        if (checkIfMultipartFileTypeIsAImage(file)) {
            return FirestoreClient
                    .getFirestore()
                    .collection(url)
                    .document("images")
                    .create(file);
        }else {
            throw new Exception("invalid file format");
        }
    }

    private boolean checkIfMultipartFileTypeIsAImage(MultipartFile file) {
        String type = file.getContentType();
        if (type != null) {
            return type.equals("image/png") || type.equals("image/jpeg");
        }
        return false;
    }
}
