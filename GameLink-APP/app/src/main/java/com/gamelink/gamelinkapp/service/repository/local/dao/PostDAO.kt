package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.PostModel

@Dao
interface PostDAO {
    @Insert
    fun save(post: PostModel)

    @Query("SELECT * FROM POSTS")
    fun list(): List<PostModel>

    @Query("SELECT * FROM POSTS WHERE user_id = :userId ORDER BY created_at DESC")
    fun listByUser(userId: Int): List<PostModel>

    @Query("SELECT * FROM POSTS WHERE visibility = :visibility ORDER BY created_at DESC")
    fun listByCommunity(visibility: Int): List<PostModel>

    @Delete
    fun delete(post: PostModel)

    @Query("SELECT * FROM POSTS WHERE id = :id AND user_id = :userId")
    fun findByIdAndUserId(id: Int, userId: Int): PostModel?
}