package com.gamelink.gamelinkapp.service.model

import com.google.gson.annotations.SerializedName


class ProfileModel {
    var id: String = ""
    var owner: String = ""
    var name: String = ""
    var profilePicPath: String? = ""
    var bannerPicPath: String? = ""
    var bio = ""
    @SerializedName("birthdayDate")
    var birthday = ""
    var showBirthday = false
    var username = ""
    var qntPosts: Int = 0
    var qntFollowers: Int = 0
    var qntFollowing: Int = 0
    var gender: String = "MALE"
    var gameTimes: List<String> = listOf()
    var icon: ImageModel = ImageModel("", "")
    var banner: ImageModel = ImageModel("", "")

}