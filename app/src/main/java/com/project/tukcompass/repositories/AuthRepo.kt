package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.ContactsRes
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
                    Resource.Success(body)
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error("Request failed with code ${response.code()}",response.code())
            }
        }catch (e: IOException){
            Log.e("SignupNetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")


        }catch (e: Exception){
            Log.e("SignupGeneralError", e.localizedMessage ?: "General Error")
            Resource.Error("Conversion Error: ${e.localizedMessage}")
        }
    }

    suspend fun login(reqBody: LoginModels): Resource<LoginResModel> {
        return try {
            val response = api.login(reqBody)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Response body is null")
                }
            } else {
                Resource.Error("Request failed with code ${response.code()}")
            }
        } catch (e: IOException) {
            Log.e("LoginNetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("LoginGeneralError", e.localizedMessage ?: "General Error")
            Resource.Error("Conversion Error: ${e.localizedMessage}")
        }
    }

    suspend fun getUserContacts(): Resource<ContactsRes> {
        return try {
            val response = api.getUserContacts()
            Log.d("CONTACTS RESPONSE", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
                Log.d("Contacts", "Response Body: ${response.body()}")
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Request failed with code ${response.code()} - ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("UnexpectedError", e.localizedMessage ?: "Unexpected Error")
            Resource.Error("Unexpected Error: ${e.localizedMessage}")
        }
    }
}