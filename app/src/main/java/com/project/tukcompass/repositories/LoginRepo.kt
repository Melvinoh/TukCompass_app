package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.RetrofitClient
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.UserModels
import com.project.tukcompass.utills.Resource
import java.io.IOException
import kotlin.math.log

class LoginRepo {

    suspend fun login(reqBody: LoginModels): Resource<LoginResModel> {
        return try {
            val response = RetrofitClient.api.login(reqBody)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.success(body)
                } else {
                    Resource.error("Response body is null")
                }
            } else {
                Resource.error("Request failed with code ${response.code()}")
            }
        } catch (e: IOException) {
            Log.e("LoginNetworkError", e.localizedMessage ?: "IO Error")
            Resource.error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("LoginGeneralError", e.localizedMessage ?: "General Error")
            Resource.error("Conversion Error: ${e.localizedMessage}")
        }
    }
}