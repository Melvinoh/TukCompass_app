package com.project.tukcompass.repositories

import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.ClubSportResponse
import com.project.tukcompass.models.CourseRequest
import com.project.tukcompass.models.CourseResponse
import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.models.TimetableResponse
import com.project.tukcompass.models.UnitContentResponse
import com.project.tukcompass.models.UnitData
import com.project.tukcompass.utills.Resource
import java.io.IOException
import java.sql.Time
import javax.inject.Inject

class AcademicRepo @Inject constructor(private val api: Api) {

    suspend fun getStudentsTimetable(): Resource<TimetableResponse> {
        return try {
            val response = api.getStudentsTimetable()
            Log.d("timetable response", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
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

    suspend fun fetchCourse( reqBody: CourseRequest): Resource<CourseResponse> {
        return try {
            val response = api.fetchCourses(reqBody)
            Log.d("course response", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
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


    suspend fun getUnitOfferingContent(unitOfferingID: String): Resource<UnitContentResponse> {
        return try {
            val response = api.getUnitOfferingContent(unitOfferingID)
            Log.d("content response", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
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


    suspend fun getUnitDetails(unitID: String): Resource<UnitData> {
        return try {
            val response = api.getUnitDetails(unitID)
            Log.d("content response", "Response Code: ${response.code()}, Message: ${response.message()}")

            if (response.isSuccessful) {
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