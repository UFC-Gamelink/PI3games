package com.gamelink.gamelinkapp.service.model

import androidx.room.Embedded
import androidx.room.Relation

data class PostProfileModel(
    val post: PostModel,
    val userProfile: ProfileModel,
    val username: String = ""
) {
}