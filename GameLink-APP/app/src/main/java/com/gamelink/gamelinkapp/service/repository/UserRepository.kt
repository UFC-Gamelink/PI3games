package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.UserAndProfileModel
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.RetrofitClient
import com.gamelink.gamelinkapp.service.repository.remote.UserDatabase
import com.gamelink.gamelinkapp.service.repository.remote.service.UserService
import com.google.gson.Gson

class UserRepository(val context: Context) {
    private val userDatabase = UserDatabase()
    private val database = LocalDatabase.getDatabase(context).userDAO()

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

    fun getUserAndProfile(userId: Int): UserAndProfileModel {
        return database.getUserAndProfile(userId)
    }

    fun update(user: UserModel) {
        database.update(user)
    }

    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }
}