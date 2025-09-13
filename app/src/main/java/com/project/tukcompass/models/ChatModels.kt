package com.project.tukcompass.models

import android.R
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatModel(
    val chatID: String = "",
    val profileUrl: String = "",
    val receiverName: String = "",
    val receiverID: String = "",
    val message:String = "",
): Parcelable

data class ChatResponse(
    val message: String = "",
    val chats: List<ChatModel>
)

data class MessageResponse(
    val response: String = "",
    val chatID: String = "",
    val messages: List<MessageModel>
)
data class  MessageModel(
    val chatID: String = "",
    val messageID: String = "",
    val messageContent: String = "",
    val mediaUrl: String? = "",
    val senderID: String = "",
    val profileUrl: String? = "",
    val senderName: String = "",
    val createdAt : String = "",
)
data class SendRes(
    val message: String = "",
    val data: List<MessageModel>,
    val lastSeen: List<Int>
)
data class SendMessage(
    val receiverID: String = "",
    val type: String = "",
    val message : String = "",
    val chatName : String? = null,
    val chatAvatar : String? = null,
    val file: String = "",
)
