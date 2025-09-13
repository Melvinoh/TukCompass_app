package com.project.tukcompass.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ClubSportReq
import com.project.tukcompass.models.ClubSportResponse

import com.project.tukcompass.models.CommentReqData
import com.project.tukcompass.models.CommentRequest
import com.project.tukcompass.models.CommentResponse
import com.project.tukcompass.models.EnrollmentStatus
import com.project.tukcompass.models.PostResponse
import com.project.tukcompass.repositories.ClubRepo
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ClubViewModel  @Inject constructor(private val repo: ClubRepo) : ViewModel() {


    private var _clubSports = MutableLiveData<Resource<ClubSportResponse>>()
    val clubSports: LiveData<Resource<ClubSportResponse>> = _clubSports

    private var _myClubSports = MutableLiveData<Resource<ClubSportResponse>>()
    val myClubSports: LiveData<Resource<ClubSportResponse>> = _myClubSports

    private var _enrolmentStatus = MutableLiveData<Resource<EnrollmentStatus>>()
    val enrollmentStatus: LiveData<Resource<EnrollmentStatus>> = _enrolmentStatus

    private var _posts = MutableLiveData<Resource<PostResponse>>()
    val posts: LiveData<Resource<PostResponse>> = _posts

    private val _postCreated = MutableLiveData<Boolean>()
    val postCreated: LiveData<Boolean> get() = _postCreated
    
    private var _comments = MutableLiveData<Resource<CommentResponse>>()
    val comment: LiveData<Resource<CommentResponse>> = _comments


//fetch clubs and sports
    fun getClubSport(){
        viewModelScope.launch {
            _clubSports.value = Resource.Loading
            val response = repo.getClubSport()
            _clubSports.postValue(response)
        }
    }

    //get clubs and sport am registered in 
    fun getMyClubs(){
        viewModelScope.launch {
            _myClubSports.value = Resource.Loading
            val response = repo.getMyClubs()
            _myClubSports.postValue(response)
        }
    }

    //enrole in club and sport 

    fun enrollClubSport(clubSportID: ClubSportReq){
        viewModelScope.launch {
            _enrolmentStatus.value = Resource.Loading
            val response = repo.enrollClubSport(clubSportID)
            _enrolmentStatus.postValue(response)
        }
    }

//fetch clubs and sports post events
    fun getPosts(id: String) {

        Log.d("clubID vm", "$id")
        viewModelScope.launch {
            _posts.value = Resource.Loading
            val response = repo.getPosts(id)
            _posts.postValue(response)
        }
    }

//create  posts for a specific club and sports 
    fun createPost(description: String, clubID: String, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
           _posts.value = Resource.Loading
            try {
                val result = repo.createPost(description, clubID, imageUri,context)
                when (result) {
                    is Resource.Success -> {
                        getPosts(clubID) // reload
                        _postCreated.value = true
                    }
                    is Resource.Error -> {
                        Log.d("error", result.message)
                        _postCreated.value = false
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                Log.e("AddComment", "Exception: ${e.message}")
                _postCreated.value = false
            }
        }
    }
    fun getComment(postID: CommentRequest) {
        viewModelScope.launch {
            val response = repo.getComments(postID)
            when (response) {
                is Resource.Success -> {
                    _comments.value = response
                }
                is Resource.Error -> {
                    Log.d("error", response.message)
                }
                else -> {}
            }
        }
    }

    fun addComment(commentData: CommentReqData) {
        viewModelScope.launch {
            try {
                val result = repo.addComments(commentData)
                when (result) {
                    is Resource.Success -> {
                        getComment(CommentRequest(commentData.postID)) // reload comments
                    }
                    is Resource.Error -> {
                        Log.d("error", result.message)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                Log.e("AddComment", "Exception: ${e.message}")
            }
        }
    }
}
