package com.project.tukcompass.models

import android.R
import android.os.Parcelable
import android.se.omapi.Session
import kotlinx.parcelize.Parcelize

data class TimeSlots (
    val startTime: String,
    val endTime: String,
    val key: String,
    val sessions: Map< String, SessionTable? >

)
data class  SessionTable(
    val unitName:String?,
    val unitID:String?,
    val unitOfferingID:String?,
    val lecturerName: String?,
    val mode: String?

)
data class  TimetableResponse(
    val timetable: List<TimeSlots>
)
@Parcelize
data class SessionDisplayItem(
    val unitName: String?,
    val unitID: String?,
    val unitOfferingID: String?,
    val lecturerName: String?,
    val mode: String?,
    val startTime: String?,
    val endTime: String?
) : Parcelable
