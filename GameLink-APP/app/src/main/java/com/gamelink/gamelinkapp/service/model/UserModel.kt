package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class UserModel {
    @SerializedName("token")
    lateinit var token: String

    var userId: String = ""

    var username: String? = ""

    var email: String = ""

    var password: String = ""
}