package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.PostProfileModel

//@Dao
interface PostDAO {
//    @Insert
    fun save(post: PostModel)

//    @Query("SELECT *, users.username FROM POSTS JOIN PROFILES ON POSTS.user_id == PROFILES.user_id JOIN users on users.id = profiles.user_id")
    fun list(): List<PostProfileModel>

//    @Query("SELECT *, users.username FROM POSTS JOIN PROFILES ON POSTS.user_id == PROFILES.user_id JOIN users on users.id = profiles.user_id WHERE POSTS.user_id = :userId ORDER BY created_at DESC")
    fun listByUser(userId: Int): List<PostProfileModel>

//    @Query("SELECT *, users.username FROM POSTS JOIN PROFILES ON POSTS.user_id == PROFILES.user_id JOIN users on users.id = profiles.user_id WHERE POSTS.visibility = :visibility ORDER BY POSTS.created_at DESC")
    fun listByCommunity(visibility: Int): List<PostProfileModel>

//    @Query("DELETE FROM POSTS WHERE post_id = :postId")
    fun delete(postId: Int)

//    @Query("SELECT * FROM POSTS JOIN PROFILES ON POSTS.user_id == PROFILES.user_id WHERE POSTS.post_id = :id AND POSTS.user_id = :userId")
    fun findByIdAndUserId(id: Int, userId: Int): PostModel?

//    @Query("DELETE FROM POSTS WHERE visibility = :id")
    fun deleteFromCommunity(id: Int)
}