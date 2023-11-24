package com.gamelink.gamelinkapp.service.repository.remote.service

import com.gamelink.gamelinkapp.service.model.ProfileModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileService {
    @POST("profile")
    suspend fun save(@Body profile: ProfileModel): Response<Boolean>

    @GET("profile")
    suspend fun get(): Response<ProfileModel?>

    @PUT("profile")
    suspend fun update(@Body profile: ProfileModel): Response<ProfileModel>
}