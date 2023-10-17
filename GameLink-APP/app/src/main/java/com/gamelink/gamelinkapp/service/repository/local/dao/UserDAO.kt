package com.gamelink.gamelinkapp.service.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gamelink.gamelinkapp.service.model.UserModel

@Dao
interface UserDAO {
    @Insert
    fun save(user: UserModel)

    @Query("SELECT * FROM USERS WHERE username = :username")
    fun login(username: String): UserModel?
}