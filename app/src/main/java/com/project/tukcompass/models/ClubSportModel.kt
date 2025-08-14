package com.project.tukcompass.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubSportModel(
    val clubSportsID: String,
    val name: String,
    val type: String,
    val profileURL: String?,
    val coverURL: String?,
    val description: String?,
    val patron: String?,
): Parcelable

@Parcelize
data class ClubSportResponse(
    val clubSports: List<ClubSportModel>,
    val message: String
) : Parcelable


data class ClubSportReq(
    val clubSportID: String,
)


data class EnrollmentStatus(
    val message: String,
)