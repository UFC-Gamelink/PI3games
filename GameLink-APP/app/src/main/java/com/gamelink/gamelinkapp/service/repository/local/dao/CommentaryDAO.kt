package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.gamelink.gamelinkapp.service.model.CommentaryModel

@Dao
interface CommentaryDAO {
    @Insert
    fun create(commentary: CommentaryModel)
}