package com.project.tukcompass

import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.AnnouncementResponse
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportResponse

import com.project.tukcompass.models.CommentReqData
import com.project.tukcompass.models.CommentRequest
import com.project.tukcompass.models.CommentResponse
import com.project.tukcompass.models.CourseRequest
import com.project.tukcompass.models.CourseResponse

import com.project.tukcompass.models.EventResponse
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.PostResponse
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.models.TimetableResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface Api {
 @POST("registration/signup")
 suspend fun signup(@Body reqBody: SignupReqModel): Response<SignupResModel>

 @POST("auth/login")
 suspend fun login(@Body reqBody: LoginModels): Response<LoginResModel>

 @POST("lecturer/fetchCourses")
 suspend fun fetchCourses(@Body reqBody: CourseRequest): Response<CourseResponse>

 @GET("events/getEvents")
 suspend fun getEvents(): Response<EventResponse>

 @GET("announcment/getAnnouncments/")
 suspend fun getAnnouncements(): Response<AnnouncementResponse>

 @GET("clubSports/getClubSport")
 suspend fun getClubSports(): Response<ClubSportResponse>

 @GET("clubSports/getMyClubs")
 suspend fun getMyClubs(): Response<ClubSportResponse>

 @GET("posts/getPosts/{id}")
 suspend fun getPosts(@Path("id") id: String): Response<PostResponse>




 @POST("comments/getComments")
 suspend fun getComments(@Body postID: CommentRequest): Response<CommentResponse>

 @GET("timetable/studentTimetable")
 suspend fun getStudentsTimetable(): Response<TimetableResponse>

 @POST("comments/addComments")
 suspend fun addComment(@Body reqBody: CommentReqData): Response<CommentResponse>


 @Multipart
 @POST("posts/addPost")
 suspend fun createPost(@Part("description") description: RequestBody, @Part image: MultipartBody.Part?, @Part("clubID") clubID: RequestBody): Response<PostResponse>

}



