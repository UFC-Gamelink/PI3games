package com.gamelink.gamelinkapp.service.model


data class PostProfileModel(
    val post: PostModel,
    val userProfile: ProfileModel,
    val username: String = ""
) {
}