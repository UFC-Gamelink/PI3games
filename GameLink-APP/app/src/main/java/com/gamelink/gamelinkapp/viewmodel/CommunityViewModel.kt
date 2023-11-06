package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository

class CommunityViewModel(application: Application): AndroidViewModel(application) {
    private val communityRepository = CommunityRepository(application.applicationContext)

    private val _community = MutableLiveData<CommunityModel>()
    val community: LiveData<CommunityModel> = _community

    fun load(id: Int) {
        _community.value = communityRepository.getById(id)
    }

}