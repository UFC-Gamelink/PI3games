package com.gamelink.gamelinkapp.service.model

import androidx.room.Embedded
import androidx.room.Relation

data class CommentaryAndProfileModel(
    @Embedded val commentary: CommentaryModel,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val profile: ProfileModel,
    val username: String = ""
) {

}