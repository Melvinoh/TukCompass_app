package com.project.tukcompass.models

data class LoginModels(
    val userID: String,
    val password: String
)
data class LoginResModel(
    val message: String,
    val token: String,
    val user: UserModels
)
