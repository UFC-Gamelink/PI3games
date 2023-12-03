package com.gamelink.gamelinkapp.service.constants

import android.Manifest


class GameLinkConstants {
    object SHARED {
        const val TOKEN_KEY = "tokenkey"
        const val USER_ID = "userid"
        const val USERNAME = "username"
    }

    object HEADER {
        const val TOKEN_KEY = "token"
    }

    object BUNDLE {
        const val COMMUNITY_ID = "community_id"
    }

    object CAMERA {
        const val TAG = "cameraX"
        const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 123
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}