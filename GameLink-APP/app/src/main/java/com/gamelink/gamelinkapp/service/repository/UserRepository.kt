package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.UserAndProfileModel
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.repository.local.LocalDatabase
import com.gamelink.gamelinkapp.service.repository.remote.RetrofitClient
import com.gamelink.gamelinkapp.service.repository.remote.UserDatabase
import com.gamelink.gamelinkapp.service.repository.remote.UserService
import com.gamelink.gamelinkapp.service.request.UserRequest
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(val context: Context) {
    private val userDatabase = UserDatabase()
    private val remote = RetrofitClient.getService(UserService::class.java)
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