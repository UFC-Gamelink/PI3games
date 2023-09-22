package com.gamelink.gamelinkapp.service.model

import com.google.gson.annotations.SerializedName

class UserModel {
    @SerializedName("token")
    lateinit var token: String
}