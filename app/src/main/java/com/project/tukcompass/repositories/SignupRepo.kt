package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.RetrofitClient
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.utills.Resource
import retrofit2.Response
import java.io.IOException

class SignupRepo {
    suspend fun signup(reqBody: SignupReqModel): Resource<SignupResModel> {
        return try {
            val response = RetrofitClient.api.signup(reqBody)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.success(body)
                } else {
                    Resource.error("Response body is null")
                }
            } else {
                Resource.error("Request failed with code ${response.code()}",response.code())
            }
        }catch (e: IOException){
            Log.e("SignupNetworkError", e.localizedMessage ?: "IO Error")
            Resource.error("Network Error: ${e.localizedMessage}")


        }catch (e: Exception){
            Log.e("SignupGeneralError", e.localizedMessage ?: "General Error")
            Resource.error("Conversion Error: ${e.localizedMessage}")
        }
    }
}