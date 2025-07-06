package com.project.tukcompass.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.repositories.HomeRepo
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel  @Inject constructor(private val repo: HomeRepo) : ViewModel(){

    private var _events = MutableLiveData<Resource<EventModel>>()
    val events: LiveData<Resource<EventModel>> = _events
    private var _announcements = MutableLiveData<Resource<AnnouncementModel>>()
    val announcements: LiveData<Resource<AnnouncementModel>> = _announcements

    private var _clubSports = MutableLiveData<Resource<ClubSportModel>>()
    val clubSports: LiveData<Resource<ClubSportModel>> = _clubSports


    fun getEvents() {
        viewModelScope.launch {
           _events.value = Resource.loading
            val response = repo.getEvents()
            _events.postValue(response)
        }
    }

    fun getAnnouncement(){
        viewModelScope.launch {
            _announcements.value = Resource.loading
            val response = repo.getAnnouncements()
            _announcements.postValue(response)
        }
    }

    fun getClubSport(){
        viewModelScope.launch {
            _clubSports.value = Resource.loading
            val response = repo.getClubSport()
            _clubSports.postValue(response)
        }
    }



}