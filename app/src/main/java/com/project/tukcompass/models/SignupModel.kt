package com.project.tukcompass.models

data class SignupModel(
    val fname: String,
    val lname: String,
    val email: String,
    val userID: String,
    val role: String,
    val courseName: String,
    val departmentName: String,
    val enrolmentYear: String,
    val password: String,
    val confirmPassword: String
)
