package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.CommentaryModel

@Dao
interface CommentaryDAO {
    @Insert
    fun create(commentary: CommentaryModel)

    @Query("SELECT * FROM commentary WHERE post_id = :postId ORDER BY id DESC")
    fun listByPost(postId: Int): List<CommentaryModel>
}