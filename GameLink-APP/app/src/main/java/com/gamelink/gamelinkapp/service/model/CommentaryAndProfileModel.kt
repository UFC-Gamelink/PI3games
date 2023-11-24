package com.gamelink.gamelinkapp.service.model

data class CommentaryAndProfileModel(
    val commentary: CommentaryModel,
    val profile: ProfileModel,
    val username: String = ""
) {

}