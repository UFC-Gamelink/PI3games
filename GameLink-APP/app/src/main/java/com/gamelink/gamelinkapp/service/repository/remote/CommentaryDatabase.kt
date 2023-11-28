package com.gamelink.gamelinkapp.service.repository.remote

import android.util.Log
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.repository.remote.service.CommentaryService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentaryDatabase {
    val remote = RetrofitClient.getService(CommentaryService::class.java)

    suspend fun save(postId: String, commentary: CommentaryModel): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                remote.save(postId, commentary)

                return@withContext true
            } catch (ex: CancellationException) {
                throw ex
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommentaryDatabase save", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }

    suspend fun get(postId: String): List<CommentaryModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.get(postId)

                if (response.code() != 200) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext response.body()!!
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommentaryDatabase get", error.message.toString())
                throw Exception(error.message.toString())
            }

        }
    }

    suspend fun delete(commentaryId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = remote.delete(commentaryId)

                if(response.code() != 202) {
                    throw Exception(response.errorBody().toString())
                }

                return@withContext true
            } catch (error: Exception) {
                error.printStackTrace()
                Log.d("CommentaryDatabase get", error.message.toString())
                throw Exception(error.message.toString())
            }
        }
    }
}