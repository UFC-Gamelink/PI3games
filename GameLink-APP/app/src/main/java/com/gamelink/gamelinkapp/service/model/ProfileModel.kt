package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Image(
    var filename: String,
    var url: String
)

//@Entity(tableName = "profiles")
class ProfileModel {
    //@PrimaryKey
    var id: String = ""

    //@ColumnInfo(name = "user_id")
    var userId: Int = 0

    //@ColumnInfo(name = "name")
    var name: String = ""

    //@ColumnInfo(name = "profile_pic_path")
    var profilePicPath: String? = ""

    //@ColumnInfo(name = "banner_pic_path")
    var bannerPicPath: String? = ""

    //@ColumnInfo(name = "bio")
    var bio = ""

    //@ColumnInfo(name = "birthday")
    @SerializedName("birthdayDate")
    var birthday = ""

   // @ColumnInfo(name = "show_birthday")
    var showBirthday = false

    var username = ""

    //@ColumnInfo(name = "num_posts")
    var numPosts: Int = 0

    var gender: String = "MALE"

    var gameTimes: List<String> = listOf()

    var icon: Image = Image("", "")

    var banner: Image = Image("", "")
}

