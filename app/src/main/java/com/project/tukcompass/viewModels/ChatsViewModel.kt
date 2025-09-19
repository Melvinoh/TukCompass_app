package com.project.tukcompass.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.DeleteResponse
import com.project.tukcompass.models.MessageModel
import com.project.tukcompass.models.MessageResponse
import com.project.tukcompass.models.SendMessage
import com.project.tukcompass.models.SendRes


import com.project.tukcompass.repositories.ChatRepo
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import com.project.tukcompass.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel@Inject constructor(private val repo: ChatRepo,private val sharedPrefManager: EncryptedSharedPrefManager) : ViewModel(){

    private var _chats = MutableLiveData<Resource<ChatResponse>>()
    val chats: LiveData<Resource<ChatResponse>> = _chats
    private val _messages = MutableLiveData<Resource<List<MessageModel>>>()
    val messages: LiveData<Resource<List<MessageModel>>> = _messages
    private val _sendResponse= MutableLiveData<SendRes>()
    val sendResponse: LiveData<SendRes> get() = _sendResponse
    private val _messageResponse= MutableLiveData<MessageResponse>()
    val messageResponse: LiveData<MessageResponse> get() = _messageResponse
    val connected: StateFlow<Boolean> = repo.connected
    private var messagesJob: Job? = null

    private val _deleteChatResult = MutableLiveData<Resource<DeleteResponse>>()
    val deleteChatResult: LiveData<Resource<DeleteResponse>> = _deleteChatResult

    private val _deleteMessageResult = MutableLiveData<Resource<DeleteResponse>>()
    val deleteMessageResult: LiveData<Resource<DeleteResponse>> = _deleteMessageResult


    fun connectSocket(baseUrl: String, token: String) {
        repo.connectSocket(baseUrl, token)
        startCollectingIncoming()
    }
    private fun startCollectingIncoming() {
        if (messagesJob?.isActive == true) return
        messagesJob = viewModelScope.launch {
            repo.incomingMessages.collect { msg ->
                val current = _messages.value
                when (current) {
                    is Resource.Success -> {
                        val updatedList = current.data.toMutableList()
                        val optimisticIndex = updatedList.indexOfFirst {
                            it.messageID.startsWith("temp_") &&
                                    it.senderID == msg.senderID &&
                                    it.messageContent == msg.messageContent
                        }
                        if (optimisticIndex != -1) {
                            updatedList[optimisticIndex] = msg
                            Log.d(
                                "ChatsViewModel",
                                "✅ Replaced optimistic msg at $optimisticIndex with ${msg.messageContent}"
                            )
                        } else {
                            updatedList.add(msg)
                            Log.d("ChatsViewModel", "✅ Added incoming msg: ${msg.messageContent}")
                        }
                        _messages.value = Resource.Success(updatedList)
                        Log.d("ChatsViewModel", "📩 messages LiveData: ${_messages.value}")
                        loadMessages(msg.chatID)
                    }

                    else -> {
                        _messages.value = Resource.Success(listOf(msg))
                        loadMessages(msg.chatID)
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

    fun deleteChat(chatId: String) {
        _deleteChatResult.value = Resource.Loading
        viewModelScope.launch {
            getUserchats()
            _deleteChatResult.value = repo.deleteChat(chatId)
        }
    }
    fun deleteMessage(messageID: String, chatID: String) {
        _deleteChatResult.value = Resource.Loading
        viewModelScope.launch {
            _deleteChatResult.value = repo.deleteMessage(messageID)
            loadMessages(chatID)
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

    fun sendMessage(
        receiverID: String,
        type: String,
        message: String,
        chatName: String,
        chatAvatar: String,
        imageUri: Uri?,
        context: Context
    ) {
        val userID = sharedPrefManager.getUser()?.userID ?: "unknown_user"
        viewModelScope.launch {
            try {
                val result = repo.sendMessage(receiverID, type, message, chatName, chatAvatar, imageUri, context)
                Log.d("ChatsViewModel", "Send response: $result")
                val optimisticMessage = MessageModel(
                    messageID = "temp_${System.currentTimeMillis()}",
                    createdAt = System.currentTimeMillis().toString(),
                    chatID = "",
                    messageContent = message,
                    mediaUrl = imageUri?.toString() ?: "",
                    senderID = userID,
                    senderName = "You",
                    profileUrl = ""
                )
                val updatedList = when (val current = _messages.value) {
                    is Resource.Success -> current.data.toMutableList().apply { add(optimisticMessage) }
                    else -> listOf(optimisticMessage)
                }
                _messages.value = Resource.Success(updatedList)
                Log.d("ChatsViewModel", "✉️ Optimistic message added: $optimisticMessage")

                if (result is Resource.Success) {
                    _sendResponse.value = result.data
                } else if (result is Resource.Error) {
                    Log.e("ChatsViewModel", "❌ Send message error: ${result.message}")
                }

            } catch (e: Exception) {
                Log.e("ChatsViewModel", "Exception sending message: ${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        repo.disconnectSocket()
    }
}



