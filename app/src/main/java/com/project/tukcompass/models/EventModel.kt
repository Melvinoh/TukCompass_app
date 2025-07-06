package com.project.tukcompass.models

data class EventModel(
    val eventID: String,
    val title: String,
    val description: String,
    val location : String,
    val date: String,
    val expiryDate: String,
    val time: String,
    val targetGroup: String,
    val fileUrl : String,
    val createdBy : String,
    val createdAt : String
)
