package com.project.tukcompass.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AnnouncementModel(
    val announcementID: String,
    val title: String,
    val message: String,
    val fileUrl: String,
    val targetGroup: String,
    val schoolID: String,
    val createdBy: String,
    val createdAt: String
) : Parcelable
@Parcelize
data class AnnouncementResponse(
    val announcements: List<AnnouncementModel>,
    val message: String
): Parcelable
