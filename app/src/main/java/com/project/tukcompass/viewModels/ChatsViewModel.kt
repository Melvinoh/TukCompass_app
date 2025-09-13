package com.project.tukcompass.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.MessageModel
import com.project.tukcompass.models.MessageResponse
import com.project.tukcompass.models.SendMessage
import com.project.tukcompass.models.SendRes


import com.project.tukcompass.repositories.ChatRepo
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel@Inject constructor(private val repo: ChatRepo) : ViewModel(){

    private var _chats = MutableLiveData<Resource<ChatResponse>>()
    val chats: LiveData<Resource<ChatResponse>> = _chats
    private val _messages = MutableStateFlow<Resource<List<MessageModel>>>(Resource.Loading)
    val messages: StateFlow<Resource<List<MessageModel>>> = _messages
    private val _sendResponse= MutableLiveData<SendRes>()
    val sendResponse: LiveData<SendRes> get() = _sendResponse
    private val _messageResponse= MutableLiveData<MessageResponse>()
    val messageResponse: LiveData<MessageResponse> get() = _messageResponse
    val connected: StateFlow<Boolean> = repo.connected
    private var messagesJob: Job? = null


    fun connectWithExistingToken(baseUrl: String, token: String) {
        repo.connectSocket(baseUrl, token)
        startCollectingIncoming()
    }
    private fun startCollectingIncoming() {
        if (messagesJob?.isActive == true) return
        messagesJob = viewModelScope.launch {
            repo.incomingMessages.collect { msg ->
                _messages.update { current ->
                    when (current) {
                        is Resource.Success -> {
                            val updatedList = current.data + msg
                            Resource.Success(updatedList ?: emptyList())
                        }
                        else -> {
                            Resource.Success(listOf(msg))
                        }
                    }
                }
            }
        }
    }
    fun joinChat(chatId: String) = repo.joinChat(chatId)
    fun getUserchats(){
        viewModelScope.launch {
            _chats.value = Resource.Loading
            val response = repo.getUserChats()
            _chats.postValue(response)
        }
    }

    fun loadMessages(chatID: String) {
        viewModelScope.launch {
            _messages.value = Resource.Loading
            try {
                val res = repo.getChatMessages(chatID)
                Log.d("ChatsViewModel", "History response: $res")

                when (res) {

                    is Resource.Success -> {
                        // res.data is MessageResponse; put the list into _messages
                        _messages.value = Resource.Success(res.data.messages)
                        _messageResponse.value = res.data
                    }
                    is Resource.Error -> {
                        _messages.value = Resource.Error(res.message)
                    }
                    else -> {}
                }
            }catch (e: Exception){
                Log.e("AddComment", "Exception: ${e.message}")
            }
        }
    }

    fun sendMessage(receiverID: String, type: String, message: String, chatName: String, chatAvatar: String, imageUri: Uri?, context: Context) {
        _messages.value = Resource.Loading
        viewModelScope.launch {
            try {
                val result = repo.sendMessage(receiverID, type, message,chatName,chatAvatar,imageUri,context)
                Log.d("send message request", "send response: $result")
                when (result) {
                    is Resource.Success -> {
                        _messages.value = Resource.Success(result.data.data)
                        _sendResponse.value = result.data
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

    override fun onCleared() {
        super.onCleared()
        repo.disconnectSocket()
    }
}
