package com.project.tukcompass.models

import android.os.Message
import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class EventModel(
    val eventID: String,
    val title: String,
    val description: String,
    val location : String,
    val date: String,
    val expiryDate: String?,
    val time: String,
    val targetGroup: String,
    val fileUrl : String,
    val createdBy : String,
    val createdAt : String
) : Parcelable
@Parcelize
data class  EventResponse(
    val message: String,
    val events: List<EventModel>
) : Parcelable
