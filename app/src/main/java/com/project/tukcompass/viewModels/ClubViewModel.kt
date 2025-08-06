package com.project.tukcompass.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ClubSportResponse

import com.project.tukcompass.models.CommentReqData
import com.project.tukcompass.models.CommentRequest
import com.project.tukcompass.models.CommentResponse
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

    private var _posts = MutableLiveData<Resource<PostResponse>>()
    val posts: LiveData<Resource<PostResponse>> = _posts


    private var _comments = MutableLiveData<Resource<CommentResponse>>()
    val comment: LiveData<Resource<CommentResponse>> = _comments



    fun getClubSport(){
        viewModelScope.launch {
            _clubSports.value = Resource.Loading
            val response = repo.getClubSport()
            _clubSports.postValue(response)
        }
    }

    fun getMyClubs(){
        viewModelScope.launch {
            _myClubSports.value = Resource.Loading
            val response = repo.getMyClubs()
            _myClubSports.postValue(response)
        }
    }


    fun getPosts(id: String) {

        Log.d("clubID vm", "$id")
        viewModelScope.launch {
            _posts.value = Resource.Loading
            val response = repo.getPosts(id)
            _posts.postValue(response)
        }
    }
    fun createPost(description: String, clubID: String, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
           _posts.value = Resource.Loading
            try {
                val result = repo.createPost(description, clubID, imageUri,context)
                when (result) {
                    is Resource.Success -> {
                        getPosts(clubID) // reload posts
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
