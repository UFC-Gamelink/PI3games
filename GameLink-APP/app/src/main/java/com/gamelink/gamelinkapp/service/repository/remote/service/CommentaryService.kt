package com.gamelink.gamelinkapp.service.repository.remote.service

import com.gamelink.gamelinkapp.service.model.CommentaryModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentaryService {
    @GET("commentaries")
    suspend fun get(@Query("postId") postId: String): Response<List<CommentaryModel>>

    @POST("commentaries")
    suspend fun save(@Query("postId") postId: String, @Body commentary: CommentaryModel): Response<Unit>

    @DELETE("commentaries/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

}