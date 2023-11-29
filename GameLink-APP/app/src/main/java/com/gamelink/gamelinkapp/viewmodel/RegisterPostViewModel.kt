package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.listener.APIListener
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.service.model.TypePostModel
import com.gamelink.gamelinkapp.service.model.ValidationModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import com.gamelink.gamelinkapp.service.repository.PostRepository
import com.gamelink.gamelinkapp.service.repository.SecurityPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLEncoder

class RegisterPostViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository = PostRepository(application.applicationContext)
    private val communityRepository = CommunityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _postSave = MutableLiveData<ValidationModel>()
    val postSave: LiveData<ValidationModel> = _postSave

    private val _communityList = MutableLiveData<List<CommunityModel>>()
    val communityList: LiveData<List<CommunityModel>> = _communityList

    fun save(post: PostModel) {
        viewModelScope.launch {
            if (post.description.isEmpty()) {
                _postSave.value = ValidationModel("Campo descrição obrigatório")
            } else {

                val description = RequestBody.create(MultipartBody.FORM, post.description)
                var imagePart: MultipartBody.Part? = null
                if (post.imageUrl != null) {
                    val imageFile = File(post.imageUrl!!)
                    val requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
                    imagePart =
                        MultipartBody.Part.createFormData("image", imageFile.name, requestImageFile)
                }

                postRepository.save(description, imagePart, object : APIListener<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        _postSave.value = ValidationModel()
                    }

                    override fun onFailure(message: String) {
                        _postSave.value = ValidationModel(message)
                    }

                })

            }
        }

    }

    fun saveForCommunity(post: PostModel, communityId: String) {
        viewModelScope.launch {
            if (post.description.isEmpty()) {
                _postSave.value = ValidationModel("Campo descrição obrigatório")
            } else {

                val description = RequestBody.create(MultipartBody.FORM, post.description)

                var imagePart: MultipartBody.Part? = null
                if (post.imageUrl != null) {
                    val imageFile = File(post.imageUrl!!)
                    val requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
                    imagePart =
                        MultipartBody.Part.createFormData("image", imageFile.name, requestImageFile)
                }

                postRepository.saveForCommunity(communityId, description, imagePart, object : APIListener<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        _postSave.value = ValidationModel()
                    }

                    override fun onFailure(message: String) {
                        _postSave.value = ValidationModel(message)
                    }

                })

            }
        }

    }

    fun loadCommunities() {
        viewModelScope.launch {
            _communityList.value = communityRepository.getMyCommunities()
        }

    }
}

