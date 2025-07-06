package com.project.tukcompass.models

data class AnnouncementModel(
    val announcmentID: String,
    val title: String,
    val message: String,
    val fileUrl: String,
    val targetGroup: String,
    val schoolID: String,
    val createdBy: String,
    val createdAt: String
)
