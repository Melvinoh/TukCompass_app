package com.project.tukcompass.models

import android.R
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatModel(
    val chatID: String = "",
    val profileUrl: String = "",
    val receiverName: String = "",
    val lastSeen: String = "",
    val message:String = "",
): Parcelable

data class ChatResponse(
    val message: String = "",
    val chats: List<ChatModel>
)

data class MessageResponse(
    val response: String = "",
    val chatID: String = "",
    val message: List<MessageModel>
)

data class  MessageModel(
    val messageID: String = "",
    val message: String = "",
    val mediaUrl: String = "",
    val senderID: String = "",
    val profileUrl: String = "",
    val senderName: String = "",
    val createdAt : String = ""
)

data class sendMessage(
    val receiverID: String = "",
    val message: String = "",
    val messageUrl : String = "",
    val mediaType: String = "",
)
