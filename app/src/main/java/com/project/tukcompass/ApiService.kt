package com.project.tukcompass

import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.models.UserModels
import com.project.tukcompass.utills.Resource
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
 @POST("registration/signup")
 suspend fun signup(@Body reqBody: SignupReqModel): Response<SignupResModel>

 @GET("auth/login")
 suspend fun login(@Body reqBody: LoginModels): Response<LoginResModel>

}
