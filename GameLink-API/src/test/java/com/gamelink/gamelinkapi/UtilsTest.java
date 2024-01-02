package com.gamelink.gamelinkapi;

import com.gamelink.gamelinkapi.utils.EnvVariablesLoader;
import org.junit.jupiter.api.Test;

class UtilsTest {
    @Test
    void test() {
        System.out.println(EnvVariablesLoader.getCloudinaryName());
    }
}
