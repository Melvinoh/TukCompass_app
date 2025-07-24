package com.project.tukcompass

import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.AnnouncementResponse
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportResponse
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.models.EventResponse
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
 @POST("registration/signup")
 suspend fun signup(@Body reqBody: SignupReqModel): Response<SignupResModel>

 @POST("auth/login")
 suspend fun login(@Body reqBody: LoginModels): Response<LoginResModel>

 @GET("events/getEvents")
 suspend fun getEvents(): Response<EventResponse>

 @GET("announcment/getAnnouncments/")
 suspend fun getAnnouncements(): Response<AnnouncementResponse>

 @GET("clubSports/getClubSport")
 suspend fun getClubSports(): Response<ClubSportResponse>

}
