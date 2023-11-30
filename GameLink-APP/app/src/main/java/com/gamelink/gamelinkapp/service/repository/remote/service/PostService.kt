package com.gamelink.gamelinkapp.service.repository.remote.service

import com.gamelink.gamelinkapp.service.model.PostModel
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
import retrofit2.http.PartMap
import retrofit2.http.Path

interface PostService {
    @Multipart
    @POST("posts")
    suspend fun save(
        @Part("description") description: RequestBody
    ): Response<Unit>

    @Multipart
    @POST("posts/image")
    suspend fun saveWithImage(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @POST("posts/event")
    suspend fun saveWithEvent(@Body post: PostModel): Response<Unit>


    @GET("posts")
    suspend fun get(): Response<List<PostModel>>

    @DELETE("posts/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

    @PUT("posts/{id}/like")
    suspend fun like(@Path("id") id: String): Response<Boolean>

    @GET("posts/recommended")
    suspend fun getRecommended(): Response<List<PostModel>>

}