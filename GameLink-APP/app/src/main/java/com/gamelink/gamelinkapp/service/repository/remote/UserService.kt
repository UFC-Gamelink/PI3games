package com.gamelink.gamelinkapp.service.repository.remote

import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.request.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/register")
    fun create(@Body body: UserRequest): Call<UserModel>
}