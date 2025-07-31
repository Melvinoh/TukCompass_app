package com.project.tukcompass.models

data class UnitModel(
    val name: String,
    val LecName: String,
    val learningMode: String

)

data class UnitRespose(
    val message: String,
    val UnitModel: List< UnitModel>
)
