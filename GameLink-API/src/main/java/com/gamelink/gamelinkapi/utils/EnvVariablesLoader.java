package com.gamelink.gamelinkapi.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvVariablesLoader {
    private EnvVariablesLoader(){}
    private static final Dotenv dotenv = Dotenv.load();

    public static String getCloudinaryName() {
        return dotenv.get("CLOUD_NAME");
    }

    public static String getCloudinaryApiSecret() {
        return dotenv.get("CLOUD_API_SECRET");
    }

    public static String getCloudinaryApiKey() {
        return dotenv.get("CLOUD_API_KEY");
    }

    public static String getSecretKey() {
        return dotenv.get("JWT_SECRET_KEY");
    }
}
