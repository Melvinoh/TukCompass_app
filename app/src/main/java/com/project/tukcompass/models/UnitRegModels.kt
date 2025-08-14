package com.project.tukcompass.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class TimeSchedule(
    var day: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var room: String = ""
)
data class ScheduleBlock(
    var courseNames: MutableList<String> = mutableListOf(),
    var timeSlots: MutableList<TimeSchedule> = mutableListOf()
)


data class UnitRegistrationRequest(
    val unitName: String,
    val academicYear: String,
    val year: String,
    val sem: String,
    val schedules: List<ScheduleBlock>
)

data class GroupedSchedulePayload(
    val courseNames: List<String>,
    val timeSlots: List<TimeSchedule>
)

data class CourseRequest(
    val courseID: String
)

data class CourseResponse(
    val courseName: List<String>
)

data class ContentGroup(
    val pdf: List<ContentItem>,
    val assignment: List<ContentItem>,
    val video: List<ContentItem>,
    val link: List<ContentItem>,
    val past_papers: List<ContentItem>
)

@Parcelize
data class ContentItem(
    val id: String,
    val title: String,
    val uploadedAt: String,
    val url: String,
    val contentType: String
):Parcelable

data class UnitContentResponse(
    val content: ContentGroup
)

data class UnitData(
    val unit: UnitInfo,
    val lecturers: List<Lecturer>,
    val courseOutline: CourseOutline,
    val pastPapers: List<PastPaper>
)

data class UnitInfo(val unitID: String, val unitName: String)

data class Lecturer(
    val fullName: String,
    val profileUrl: String?,
    val academicYear: String,
    val unitOfferingID: String
)

data class CourseOutline(
    val courseOutline: String,
    val description: String,
    val uploadedBy: String,
    val uploadDate: String
)
@Parcelize
data class PastPaper(
    val PdfUrl: String,
    val fileName: String,
    val uploadedAt: String
):Parcelable
