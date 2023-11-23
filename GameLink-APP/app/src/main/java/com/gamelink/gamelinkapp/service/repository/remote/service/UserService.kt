package com.gamelink.gamelinkapp.service.repository.remote.service

import com.gamelink.gamelinkapp.service.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/register")
    suspend fun create(@Body body: UserModel): Response<UserModel>

    @POST("auth/authenticate")
    suspend fun login(@Body body: UserModel): Response<UserModel>
}