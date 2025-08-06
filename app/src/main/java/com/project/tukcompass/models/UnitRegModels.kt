package com.project.tukcompass.models

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