package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
class ProfileModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "user_id")
    var userId: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "profile_pic_path")
    var profilePicPath = ""

    @ColumnInfo(name = "banner_pic_path")
    var bannerPicPath = ""

    @ColumnInfo(name = "bio")
    var bio = ""

    @ColumnInfo(name = "birthday")
    var birthday = ""

    @ColumnInfo(name = "show_birthday")
    var showBirthday = false
}