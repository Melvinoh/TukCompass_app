package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.utills.Resource
import java.io.IOException
import javax.inject.Inject

class AuthRepo @Inject constructor(private val api: Api) {

    suspend fun signup(reqBody: SignupReqModel): Resource<SignupResModel> {
        return try {
            val response = api.signup(reqBody)

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

    suspend fun login(reqBody: LoginModels): Resource<LoginResModel> {
        return try {
            val response = api.login(reqBody)
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