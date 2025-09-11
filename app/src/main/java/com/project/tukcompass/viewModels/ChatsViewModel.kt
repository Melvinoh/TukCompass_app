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

    private var _messages =  MutableStateFlow<Resource<MessageResponse>>()
    val message:StateFlow<Resource<MessageResponse>> = _messages

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
                _messages.update { it + msg }
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
    fun sendMessage(chatId: String, text: String) {
        repo.sendMessage(OutgoingMessage(chatId = chatId, message = text))
        // Optional: optimistic UI update
        val nowIso = java.time.Instant.now().toString()
        val temp = IncomingMessage(
            id = null,
            chatId = chatId,
            senderId = "me",
            message = text,
            createdAt = nowIso
        )
        _messages.update { it + temp }
    }
    override fun onCleared() {
        super.onCleared()
        repo.disconnectSocket()
    }
    
    




}
