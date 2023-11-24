package com.gamelink.gamelinkapp.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "users")
class UserModel {
    @SerializedName("token")
//    @Ignore
    lateinit var token: String

//    @PrimaryKey(autoGenerate = true)
    var id: String = ""

//    @ColumnInfo(name = "username")
    var username: String? = ""

//    @ColumnInfo(name = "email")
    var email: String = ""

//    @ColumnInfo(name = "password")
    var password: String = ""
}