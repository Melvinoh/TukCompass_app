package com.project.tukcompass

import com.project.tukcompass.models.SignupModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
 @POST("registration/signup")
 suspend fun signup(@Body reqBody: SignupModel): Response<SignupModel>
}