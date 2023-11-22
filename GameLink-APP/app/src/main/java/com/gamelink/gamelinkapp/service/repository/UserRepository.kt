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

    fun login(username: String, password: String): UserModel? {
        val user = database.login(username) ?: return null

        return if(user.password == password) {
            user
        } else {
            null
        }


    }

    fun getUserAndProfile(userId: Int): UserAndProfileModel {
        return database.getUserAndProfile(userId)
    }

    fun update(user: UserModel) {
        database.update(user)
    }

//    fun create(
//        username: String,
//        email: String,
//        password: String,
//        listener: APIListener<UserModel>
//    ) {
//        val call = remote.create(UserRequest(username, email, password))
//
//        call.enqueue(object : Callback<UserModel> {
//            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
//                if (response.code() == 201) {
//                    response.body()?.let { listener.onSuccess(it) }
//                } else {
//                    listener.onFailure(response.errorBody()!!.string())
//                }
//            }
//
//            override fun onFailure(call: Call<UserModel>, t: Throwable) {
//                listener.onFailure(context.getString(R.string.UNEXPECTED_ERROR))
//            }
//        })
//    }

    fun login(username: String, password: String, listener: APIListener<UserModel>) {
        val call = remote.login(UserRequest(username = username, password = password))

        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.code() == 200) {
                    response.body()?.let { listener.onSuccess(it) }
                } else if (response.code() == 204) {
                    listener.onFailure(context.getString(R.string.USER_NOT_FOUND))
                } else {
                    listener.onFailure(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.UNEXPECTED_ERROR))
            }
        })
    }

    private fun failResponse(str: String): String {
        return Gson().fromJson(str, String::class.java)
    }
}