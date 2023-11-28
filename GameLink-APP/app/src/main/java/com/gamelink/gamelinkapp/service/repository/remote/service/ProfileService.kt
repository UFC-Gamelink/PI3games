package com.gamelink.gamelinkapp.service.repository.remote.service

import com.gamelink.gamelinkapp.service.model.ProfileModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProfileService {
    @POST("profile")
    suspend fun save(@Body profile: ProfileModel): Response<Unit>

    @GET("profile")
    suspend fun get(): Response<ProfileModel?>

    @PUT("profile")
    suspend fun update(@Body profile: ProfileModel): Response<ProfileModel>

    @Multipart
    @POST("profile/images")
    suspend fun saveImages(
        @Part icon: MultipartBody.Part,
        @Part banner: MultipartBody.Part
    ): Response<ProfileModel>

    @Multipart
    @PUT("profile/images")
    suspend fun updateOnlyIcon(
        @Part icon: MultipartBody.Part
    ): Response<ProfileModel>

    @Multipart
    @PUT("profile/images")
    suspend fun updateOnlyBanner(
        @Part banner: MultipartBody.Part
    ): Response<ProfileModel>

    @Multipart
    @PUT("profile/images")
    suspend fun updateImages(
        @Part icon: MultipartBody.Part,
        @Part banner: MultipartBody.Part
    ): Response<ProfileModel>
}