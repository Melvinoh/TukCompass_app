package com.project.tukcompass.repositories

import com.project.tukcompass.RetrofitClient
import com.project.tukcompass.models.SignupModel
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SignupRepo {
    suspend fun signup(reqBody: SignupModel): Response<SignupModel> {
        return try {
            RetrofitClient.api.signup(reqBody)
        } catch (e: IOException) {
            Response.error(500, null)
        }
    }
}