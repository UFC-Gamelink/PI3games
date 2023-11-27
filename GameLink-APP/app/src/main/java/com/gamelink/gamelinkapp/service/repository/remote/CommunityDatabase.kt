package com.gamelink.gamelinkapp.service.repository.remote

import android.util.Log
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.remote.service.CommunityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommunityDatabase {
    private val remote = RetrofitClient.getService(CommunityService::class.java)

    suspend fun save(community: CommunityModel): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                remote.save(community)

                return@withContext true
            }catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase save", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun listAll(): List<CommunityModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.getAll()

                return@withContext response.body()!!
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("PostDatabase get", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }
}