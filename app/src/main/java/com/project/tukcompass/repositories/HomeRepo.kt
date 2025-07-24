package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.AnnouncementResponse
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportResponse
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.models.EventResponse
import com.project.tukcompass.utills.Resource
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

    suspend fun getClubSport(): Resource<ClubSportResponse>{
        val response = api.getClubSports()
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




}