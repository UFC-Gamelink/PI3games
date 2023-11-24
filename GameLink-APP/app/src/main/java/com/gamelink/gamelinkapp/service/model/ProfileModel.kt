package com.gamelink.gamelinkapp.service.model

import com.google.gson.annotations.SerializedName

data class Image(
    var filename: String,
    var url: String
)

class ProfileModel {
    var id: String = ""
    var userId: Int = 0
    var name: String = ""
    var profilePicPath: String? = ""
    var bannerPicPath: String? = ""
    var bio = ""
    @SerializedName("birthdayDate")
    var birthday = ""
    var showBirthday = false
    var username = ""
    var numPosts: Int = 0
    var gender: String = "MALE"
    var gameTimes: List<String> = listOf()
    var icon: Image = Image("", "")
    var banner: Image = Image("", "")
}