package com.gamelink.gamelinkapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.repository.CommunityRepository
import kotlinx.coroutines.launch

class CommunitiesViewModel(application: Application): AndroidViewModel(application) {
    private val communitiesRepository = CommunityRepository(application.applicationContext)

    private val _communities = MutableLiveData<List<CommunityModel>>()
    val communities: LiveData<List<CommunityModel>> = _communities

    fun list() {
        viewModelScope.launch {
            _communities.value = communitiesRepository.list()
        }

    }
}