package com.project.tukcompass.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.MessageResponse


import com.project.tukcompass.repositories.ChatRepo
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel@Inject constructor(private val repo: ChatRepo) : ViewModel(){

    private var _chats = MutableLiveData<Resource<ChatResponse>>()
    val chats: LiveData<Resource<ChatResponse>> = _chats

    private var _messages = MutableLiveData<Resource<MessageResponse>>()
    val message: LiveData<Resource<MessageResponse>> = _messages

    fun getUserchats(){
        viewModelScope.launch {
            _chats.value = Resource.Loading
            val response = repo.getUserChats()
            _chats.postValue(response)
        }
    }

    fun getPosts(chatID: String) {

        Log.d("clubID vm", "$chatID")
        viewModelScope.launch {
            _messages.value = Resource.Loading
            val response = repo.getChatMessages(chatID)
            _messages.postValue(response)
        }
    }



}