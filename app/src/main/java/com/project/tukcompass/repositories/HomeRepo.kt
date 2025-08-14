package com.project.tukcompass.repositories

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.AnnouncementResponse
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportResponse
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.models.EventRequest
import com.project.tukcompass.models.EventResponse
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.utills.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class HomeRepo @Inject constructor(private val api: Api) {

   suspend fun getEvents() : Resource<EventResponse> {
        return try {
            val response = api.getEvents()
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
        }catch (e: IOException) {
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")
        }

   }

    suspend fun  getAnnouncements() : Resource<AnnouncementResponse> {

        val response = api.getAnnouncements()
        return try {
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
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.Error("Network Error: ${e.localizedMessage}")
        }
    }

    suspend fun getMyClubs(): Resource<ClubSportResponse> {
        return try {
            val response = api.getMyClubs()
            Log.d("club response", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
                Log.d("ClubAPI", "Response Body: ${response.body()!!.message}")
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

    suspend fun addEvent(event: EventRequest, imageUri: Uri?, context: Context): Resource<EventResponse> {
        return try {
            Log.d("repo log", "${event}")
            val titlePart = event.title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descPart = event.description.toRequestBody("text/plain".toMediaTypeOrNull())
            val locPart = event.location.toRequestBody("text/plain".toMediaTypeOrNull())
            val datePart = event.date.toRequestBody("text/plain".toMediaTypeOrNull())
            val timePart = event.time.toRequestBody("text/plain".toMediaTypeOrNull())

            Log.d("timePart", "${timePart}")
            var imagePart: MultipartBody.Part? = null
            imageUri?.let {
                val file = File(getPathFromUri(context, it))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
            val response = api.addEvent(titlePart, descPart, locPart, datePart, timePart, imagePart)
            Log.d("request log", "${response}")
            if (response.isSuccessful) {
                Log.d("request log", "${response}")
                Log.d("request log", "${response.message()}")

                return response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                return Resource.Error("Failed with code ${response.code()}: ${response.message()}")
            }


        } catch (e: IOException) {
            Log.e("CreatePostError", "IO: ${e.localizedMessage}")
            Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("CreatePostError", "Unknown: ${e.localizedMessage}")
            Resource.Error("Unexpected Error: ${e.localizedMessage}")
        }








    }
    private fun getPathFromUri(context: Context, uri: Uri): String {
        var path = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (idx != -1) path = it.getString(idx)
        }
        return path
    }





}