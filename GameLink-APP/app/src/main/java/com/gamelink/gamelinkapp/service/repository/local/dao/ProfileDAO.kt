package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.ProfileModel

@Dao
interface ProfileDAO {
    @Insert
    fun save(profile: ProfileModel)

    @Query("SELECT * FROM profiles WHERE user_id = :userId")
    fun getByUserId(userId: Int): ProfileModel?
}