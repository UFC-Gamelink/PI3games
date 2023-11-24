package com.gamelink.gamelinkapp.service.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndProfileModel(
    @Embedded val user: UserModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val profile: ProfileModel
)