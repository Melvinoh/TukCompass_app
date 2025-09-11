package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.ClubSportResponse
import com.project.tukcompass.models.MessageResponse
import com.project.tukcompass.models.PostResponse
import com.project.tukcompass.utills.Resource
import java.io.IOException
import javax.inject.Inject

class ChatRepo @Inject constructor(private val api: Api, private val socketManager: SocketManager) {

    val incomingMessages: SharedFlow<IncomingMessage> = socketManager.incomingMessages
    val connected: StateFlow<Boolean> = socketManager.connected

    suspend fun getUserChats(): Resource<ChatResponse> {
        return try {
            val response = api.getUserChats()
            Log.d("club response", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
                Log.d("ChatAPI", "Response Body: ${response.body()!!.message}")
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Request failed with code ${response.code()} - ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UnexpectedError", e.localizedMessage ?: "Unexpected Error")
            Resource.Error("Unexpected Error: ${e.localizedMessage}")
        }
    }

    suspend fun getChatMessages(chatID: String) : Resource<MessageResponse> {

        return try {
            val response = api.getChatMessages(chatID)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)

                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error("Request failed with code ${response.code()}")
            }
        }catch (e: IOException) {
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")
        }
    }
    fun connectSocket(serverUrl: String, token: String) {
        socketManager.connect(serverUrl, token)
    }
     fun disconnectSocket() {
        socketManager.disconnect()
    }
     fun joinChat(chatId: String) = socketManager.joinChat(chatId)

     fun sendMessage(msg: OutgoingMessage) = socketManager.sendMessage(msg)

    
   

    

}
