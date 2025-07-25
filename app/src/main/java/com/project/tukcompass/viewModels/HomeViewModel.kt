package com.project.tukcompass.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.AnnouncementResponse
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportResponse
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.models.EventResponse
import com.project.tukcompass.repositories.HomeRepo
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel  @Inject constructor(private val repo: HomeRepo) : ViewModel(){

    private var _events = MutableLiveData<Resource<EventResponse>>()
    val events: LiveData<Resource<EventResponse>> = _events
    private var _announcements = MutableLiveData<Resource<AnnouncementResponse>>()
    val announcements: LiveData<Resource<AnnouncementResponse>> = _announcements

    private var _clubSports = MutableLiveData<Resource<ClubSportResponse>>()
    val clubSports: LiveData<Resource<ClubSportResponse>> = _clubSports

    fun getEvents() {
        viewModelScope.launch {
           _events.value = Resource.Loading
            val response = repo.getEvents()
            _events.postValue(response)
        }
    }
    fun getAnnouncement(){
        viewModelScope.launch {
            _announcements.value = Resource.Loading
            val response = repo.getAnnouncements()
            _announcements.postValue(response)
        }
    }
    fun getMyClubs(){
        viewModelScope.launch {
            _clubSports.value = Resource.Loading
            val response = repo.getMyClubs()
            _clubSports.postValue(response)
        }
    }
}