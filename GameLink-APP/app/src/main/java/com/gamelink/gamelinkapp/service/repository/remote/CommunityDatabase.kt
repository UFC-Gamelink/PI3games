package com.gamelink.gamelinkapp.service.repository.remote

import android.util.Log
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.remote.service.CommunityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class CommunityDatabase {
    private val remote = RetrofitClient.getService(CommunityService::class.java)

    suspend fun save(community: CommunityModel): CommunityModel {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.save(community)

                return@withContext response.body()!!
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase save", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun saveBanner(communityId: String, banner: MultipartBody.Part): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.saveImage(banner, communityId)

                if (response.code() != 201) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext true
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommunityDatabase saveBanner", error.message.toString())
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
                Log.d("PostDatabase listAll", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun get(id: String): CommunityModel {
        try {
            val response = remote.get(id)

            if (response.code() != 200) {
                throw Exception(response.errorBody().toString())
            }

            return response.body()!!
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("PostDatabase get", error.message.toString())
            throw Exception(error.message.toString())
        }
    }

    suspend fun delete(id: String) {
        try {
            remote.delete(id)
        } catch (error: Exception) {
            error.printStackTrace()
            Log.d("PostDatabase delete", error.message.toString())
            throw Exception(error.message.toString())
        }
    }
}