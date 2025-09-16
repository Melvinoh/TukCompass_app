package com.project.tukcompass.models

data class SignupReqModel(
    val fname: String,
    val sname: String,
    val email: String,
    val userID: String,
    val role: String,
    val courseName: String,
    val departmentName: String,
    val enrolmentYear: String,
    val password: String,
    val confirmPassword: String
)
