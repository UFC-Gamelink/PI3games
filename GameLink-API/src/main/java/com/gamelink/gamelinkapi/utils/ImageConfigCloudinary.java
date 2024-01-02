package com.gamelink.gamelinkapi.utils;

import com.cloudinary.utils.ObjectUtils;

import java.util.Map;

public class ImageConfigCloudinary {
    private ImageConfigCloudinary(){}
    public static Map getConfig() {
        return ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );
    }
}
