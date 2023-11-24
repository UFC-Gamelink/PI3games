package com.gamelink.gamelinkapp.service.model

import androidx.room.Embedded
import androidx.room.Relation

data class PostProfileModel(
//    @Embedded
    val post: PostModel,
//    @Relation(
//        parentColumn = "user_id",
//        entityColumn = "user_id"
//    )
    val userProfile: ProfileModel,
    val username: String = ""
) {
}