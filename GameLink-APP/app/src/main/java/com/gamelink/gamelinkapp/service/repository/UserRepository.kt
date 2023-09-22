package com.gamelink.gamelinkapp.service.repository

import android.content.Context
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.UserModel
import com.gamelink.gamelinkapp.service.repository.remote.RetrofitClient
import com.gamelink.gamelinkapp.service.repository.remote.UserService
import com.gamelink.gamelinkapp.service.request.UserRequest
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(val context: Context) {
    private val remote = RetrofitClient.getService(UserService::class.java)

    fun create(username: String, email: String, password: String, listener: APIListener<UserModel>) {
        val call = remote.create(UserRequest(username, email, password))

        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if(response.code() == 201) {
                    response.body()?.let { listener.onSuccess(it) }
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