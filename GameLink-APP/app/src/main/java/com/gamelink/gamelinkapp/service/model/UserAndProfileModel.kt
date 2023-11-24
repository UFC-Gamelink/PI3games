package com.gamelink.gamelinkapp.service.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndProfileModel(
    val user: UserModel,
    val profile: ProfileModel
)