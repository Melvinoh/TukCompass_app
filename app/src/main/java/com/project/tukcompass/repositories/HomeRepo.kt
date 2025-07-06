package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.utills.Resource
import java.io.IOException
import javax.inject.Inject

class HomeRepo @Inject constructor(private val api: Api) {

   suspend fun getEvents() : Resource<EventModel> {
        return try {
            val response = api.getEvents()
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
        }catch (e: IOException) {
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.error("Network Error: ${e.localizedMessage}")
        }

   }

    suspend fun  getAnnouncements() : Resource<AnnouncementModel> {

        val response = api.getAnnouncements()
        return try {
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
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.error("Network Error: ${e.localizedMessage}")
        }
    }

    suspend fun getClubSport(): Resource<ClubSportModel>{
        val response = api.getClubSports()
        return try {
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
            Log.e("NetworkError", e.localizedMessage ?: "IO Error")
            Resource.error("Network Error: ${e.localizedMessage}")
        }
    }




}