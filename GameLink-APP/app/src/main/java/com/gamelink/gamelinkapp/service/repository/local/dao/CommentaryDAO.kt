package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.gamelink.gamelinkapp.service.model.CommentaryAndProfileModel
import com.gamelink.gamelinkapp.service.model.CommentaryModel

//@Dao
interface CommentaryDAO {
//    @Insert
    fun create(commentary: CommentaryModel)

//    @Query("SELECT * FROM commentary WHERE post_id = :postId ORDER BY comment_id DESC")
    fun listByPost(postId: Int): List<CommentaryModel>

//    @Transaction
//    @Query("SELECT *, users.username FROM commentary JOIN users on users.id = commentary.user_id WHERE post_id = :postId ORDER BY id DESC")
    fun listWithProfileByPost(postId: Int): List<CommentaryAndProfileModel>

//    @Query("DELETE FROM commentary WHERE comment_id = :commentaryId")
    fun delete(commentaryId: Int)
}