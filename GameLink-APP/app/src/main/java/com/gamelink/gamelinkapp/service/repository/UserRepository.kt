package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.repository.remote.UserDatabase

class UserRepository(val context: Context) {
    private val userDatabase = UserDatabase()

    suspend fun create(user: UserModel, listener: APIListener<Boolean>) {
        try {
            userDatabase.create(user)

            listener.onSuccess(true)
        } catch(e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }

    suspend fun login(user: UserModel, listener: APIListener<UserModel>) {
        try {
            val userResponse = userDatabase.login(user)

            listener.onSuccess(userResponse)
        } catch(e: Exception) {
            listener.onFailure(e.message.toString())
        }
    }
}