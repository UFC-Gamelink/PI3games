package com.gamelink.gamelinkapp.service.repository.remote.service

import com.gamelink.gamelinkapp.service.model.CommunityModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CommunityService {
    @GET("communities")
    suspend fun getAll(): Response<List<CommunityModel>>

    @GET("communities/{id}")
    suspend fun get(@Path("id") id: String): Response<CommunityModel>

    @POST("communities")
    suspend fun save(@Body community: CommunityModel): Response<CommunityModel>

    @Multipart
    @POST("communities/banner/{id}")
    suspend fun saveImage(
        @Part banner: MultipartBody.Part,
        @Path("id") id: String
    ): Response<CommunityModel>

    @PUT("communities/{id}")
    suspend fun update(@Path("id") id: String, @Body community: CommunityModel): Response<Unit>

    @Multipart
    @PUT("communities/banner/{id}")
    suspend fun updateImage(
        @Path("id") id: String,
        @Part banner: MultipartBody.Part
    ): Response<Unit>

    @PUT("communities/{id}/enter")
    suspend fun enter(@Path("id") id: String): Response<Unit>

    @PUT("communities/{id}/exit")
    suspend fun exit(@Path("id") id: String): Response<Unit>

    @Multipart
    @PUT("communities/{id}/post")
    suspend fun makePost(
        @Path("id") id: String,
        @Part("description") description: RequestBody
    ): Response<Unit>

    @Multipart
    @PUT("communities/{id}/post/image")
    suspend fun makePostWithImage(
        @Path("id") id: String,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @DELETE("/communities/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

}