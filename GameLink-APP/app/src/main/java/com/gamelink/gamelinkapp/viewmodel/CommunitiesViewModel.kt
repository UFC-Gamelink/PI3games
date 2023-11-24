package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository

class CommunitiesViewModel(application: Application): AndroidViewModel(application) {
    private val communitiesRepository = CommunityRepository(application.applicationContext)

    private val _communities = MutableLiveData<List<CommunityModel>>()
    val communities: LiveData<List<CommunityModel>> = _communities

    fun list() {
        _communities.value = communitiesRepository.list()
    }
}