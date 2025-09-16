package com.project.tukcompass.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class UnitModel(
    val name: String = " ",
    val LecName: String = "",
    val learningMode: String = "",

)

data class UnitRespose(
    val message: String,
    val UnitModel: List< UnitModel>
)


data class CourseUnitsResponse(
    val courseID: String = "",
    val grouped: List<YearTab>
)
@Parcelize
data class YearTab(
    val year: Int,
    val semesters: List<SemesterTab>
) : Parcelable

@Parcelize
data class SemesterTab(
    val sem: Int,
    val units: List<UnitItem>
) : Parcelable

@Parcelize
data class UnitItem(
    val unitID: String = "",
    val courseUnitID: String = "",
    val unitName: String? = null,
    val unitDescription: String? = null
) : Parcelable
