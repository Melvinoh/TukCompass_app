package com.project.tukcompass.socket

import android.util.Log
import com.project.tukcompass.models.MessageModel
import com.project.tukcompass.models.SendMessage
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import javax.inject.Singleton

@Singleton
class SocketManager {
    private var socket: Socket? = null
    private val _connected = MutableStateFlow(false)
    val connected: StateFlow<Boolean> = _connected
    private val _incomingMessages = MutableSharedFlow<MessageModel>(extraBufferCapacity = 64)
    val incomingMessages: SharedFlow<MessageModel> = _incomingMessages
    fun connect(baseUrl: String, token: String) {
        if (socket?.connected() == true) return

        val opts = IO.Options().apply {
            reconnection = true
            reconnectionAttempts = Int.MAX_VALUE
            reconnectionDelay = 1000
            query = "token=$token"
        }
        socket = IO.socket(baseUrl, opts)

        socket?.on(Socket.EVENT_CONNECT) {
            _connected.tryEmit(true)
        }?.on(Socket.EVENT_DISCONNECT) {
            _connected.tryEmit(false)
        }?.on("new_message") { args ->
            (args.firstOrNull() as? JSONObject)?.let { json ->
                try {
                    val msg = MessageModel(
                        messageID = json.optString("messageID", ""),
                        createdAt = json.optString("createdAt", ""),
                        chatID = json.optString("chatID", ""),
                        messageContent = json.optString("message", ""),
                        mediaUrl = json.optString("mediaUrl", ""),
                        senderID = json.optString("senderID", ""),
                        senderName = json.optString("senderName", ""),
                        profileUrl = json.optString("profileUrl", "")
                    )
                    _incomingMessages.tryEmit(msg)
                } catch (e: Exception) {
                    Log.e("SocketManager", "Error parsing new_message: ${e.message}")
                }
            }
        }

        socket?.connect() // connect once, not inside new_message listener
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
        socket = null
        _connected.tryEmit(false)
    }

    fun joinChat(chatId: String) {
        val payload = JSONObject().apply { put("chatID", chatId) }
        socket?.emit("join_chat", payload)
    }

    fun sendMessage(out: SendMessage) {
        val obj = JSONObject().apply {
            put("receiverID", out.receiverID)
            put("type", out.type)
            put("message", out.message)
            out.chatName?.let { put("chatName", it) }       // keep chatName under chatName
            out.chatAvatar?.let { put("chatAvatar", it) }   // keep chatAvatar under chatAvatar
            if (out.file.isNotBlank()) put("file", out.file) // if you pass file URL/string
        }
        socket?.emit("send_message", obj)
    }
}



