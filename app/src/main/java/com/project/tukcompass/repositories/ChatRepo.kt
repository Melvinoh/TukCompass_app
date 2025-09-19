package com.project.tukcompass.repositories

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.DeleteResponse
import com.project.tukcompass.models.MessageModel
import com.project.tukcompass.models.MessageResponse
import com.project.tukcompass.models.SendRes
import com.project.tukcompass.socket.SocketManager
import com.project.tukcompass.utills.Resource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject



class ChatRepo @Inject constructor(private val api: Api, private val socketManager: SocketManager) {

    val incomingMessages: SharedFlow<MessageModel> = socketManager.incomingMessages
    val connected: StateFlow<Boolean> = socketManager.connected

    suspend fun getUserChats(): Resource<ChatResponse> {
        return try {
            val response = api.getUserChats()
            Log.d("CHAT RESPONSE", "Response Code: ${response.code()}, Message: ${response.message()}")

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
            Log.d("CHAT RESPONSE", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
                Log.d("ChatAPI", "Response Body: ${response.body()!!.messages}")
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
    suspend fun deleteChat(chatId: String): Resource<DeleteResponse> {
        return try {
            val response = api.deleteChat(chatId)
            if (response.isSuccessful) {
                Log.d("DELETE CHAT", "Response Body: ${response.body()!!.message}")
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)

                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Delete failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unexpected error")
        }
    }

    suspend fun deleteMessage(messageID: String): Resource<DeleteResponse> {
        return try {
            val response = api.deleteMessage(messageID)
            if (response.isSuccessful) {
                Log.d("DELETE MESSAGE", "Response Body: ${response.body()!!.message}")
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)

                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Failed to delete chat")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unexpected error")
        }
    }
    fun connectSocket(serverUrl: String, token: String) {
        socketManager.connect(serverUrl, token)
    }
     fun disconnectSocket() {
        socketManager.disconnect()
    }
     fun joinChat(chatId: String) = socketManager.joinChat(chatId)


    suspend fun sendMessage(
        receiverID: String,
        type: String,
        message: String,
        chatName: String?,
        chatAvatar: String?,
        imageUri: Uri?,
        context: Context
    ): Resource<SendRes> {
        return try {
            val receiverPart = receiverID.toRequestBody("text/plain".toMediaTypeOrNull())
            val typePart = type.toRequestBody("text/plain".toMediaTypeOrNull())
            val messagePart = message.toRequestBody("text/plain".toMediaTypeOrNull())
            val chatNamePart = chatName?.toRequestBody("text/plain".toMediaTypeOrNull())
            val chatAvatarPart = chatAvatar?.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageUri?.let {
                val file = File(getRealPathFromUri(context, it))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", file.name, requestFile)
            }

            val response = api.sendMessageAPI(
                receiverPart,
                typePart,
                messagePart,
                chatNamePart,
                chatAvatarPart,
                imagePart
            )
            Log.d("CHAT RESPONSE", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Failed with code ${response.code()}: ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("NetworkError", "IO: ${e.localizedMessage}")
            Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("CreatePostError", "Unknown: ${e.localizedMessage}")
            Resource.Error("Unexpected Error: ${e.localizedMessage}")
        }
    }

    private fun getRealPathFromUri(context: Context, uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path ?: ""
    }

}
