package com.project.tukcompass

import com.project.tukcompass.models.AnnouncementModel
import com.project.tukcompass.models.AnnouncementResponse
import com.project.tukcompass.models.ChatResponse
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.ClubSportReq
import com.project.tukcompass.models.ClubSportResponse

import com.project.tukcompass.models.CommentReqData
import com.project.tukcompass.models.CommentRequest
import com.project.tukcompass.models.CommentResponse
import com.project.tukcompass.models.ContactsRes
import com.project.tukcompass.models.CourseRequest
import com.project.tukcompass.models.CourseResponse
import com.project.tukcompass.models.EnrollmentStatus
import com.project.tukcompass.models.EventRequest

import com.project.tukcompass.models.EventResponse
import com.project.tukcompass.models.LoginModels
import com.project.tukcompass.models.LoginResModel
import com.project.tukcompass.models.MessageResponse
import com.project.tukcompass.models.PostResponse
import com.project.tukcompass.models.SendRes
import com.project.tukcompass.models.SignupReqModel
import com.project.tukcompass.models.SignupResModel
import com.project.tukcompass.models.TimeSlots
import com.project.tukcompass.models.TimetableResponse
import com.project.tukcompass.models.UnitContentResponse
import com.project.tukcompass.models.UnitData
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


 //events & announcement
 @Multipart
 @POST("events/createEvent")
 suspend fun addEvent(
  @Part("title") title: RequestBody,
  @Part("description") description: RequestBody,
  @Part("location") location: RequestBody,
  @Part("date") date: RequestBody,
  @Part("time") time: RequestBody,
  @Part image: MultipartBody.Part?
 ): Response<EventResponse>


 @GET("events/getEvents")
 suspend fun getEvents(): Response<EventResponse>

 @GET("announcment/getAnnouncments/")
 suspend fun getAnnouncements(): Response<AnnouncementResponse>

 // club & sports
 @POST("clubSports/enrollClubSport")
 suspend fun enrollClubSport(@Body clubSportID: ClubSportReq): Response<EnrollmentStatus>

 @GET("clubSports/getClubSport")
 suspend fun getClubSports(): Response<ClubSportResponse>

 @GET("clubSports/getMyClubs")
 suspend fun getMyClubs(): Response<ClubSportResponse>

 //posts endpoints
 @Multipart
 @POST("posts/addPost")
 suspend fun createPost(
  @Part("description") description: RequestBody,
  @Part image: MultipartBody.Part?,
  @Part("clubID") clubID: RequestBody,

 ): Response<PostResponse>

 @GET("posts/getPosts/{id}")
 suspend fun getPosts(@Path("id") id: String): Response<PostResponse>

//comments endpoints

 @POST("comments/addComments")
 suspend fun addComment(@Body reqBody: CommentReqData): Response<CommentResponse>

 @POST("comments/getComments")
 suspend fun getComments(@Body postID: CommentRequest): Response<CommentResponse>


 // Academics endpoints

 @POST("lecturer/fetchCourses")
 suspend fun fetchCourses(@Body reqBody: CourseRequest): Response<CourseResponse>

 @GET("timetable/studentTimetable")
 suspend fun getStudentsTimetable(): Response<TimetableResponse>


 @GET("unitContent/getUnitOfferingContent/{unitOfferingID}")
 suspend fun getUnitOfferingContent(@Path("unitOfferingID") offeringId: String): Response<UnitContentResponse>

 @GET("unitContent/getUnitDetails/{unitID}")
 suspend fun getUnitDetails(@Path("unitID") unitID: String): Response<UnitData>



 //chat endpoints
 @GET("chats/getUserChats")
 suspend fun getUserChats(): Response<ChatResponse>

 @GET("registration/getContacts")
 suspend fun getUserContacts(): Response<ContactsRes>

 @GET("chats/getChatMessages/{chatID}")
 suspend fun getChatMessages(@Path("chatID") chatID: String): Response<MessageResponse>
 @Multipart
 @POST("chats/sendMessage")
 suspend fun sendMessageAPI(
  @Part("receiverID") receiverID: RequestBody,
  @Part("type") type: RequestBody,
  @Part("message") message: RequestBody,
  @Part("chatName") chatName: RequestBody?,
  @Part("chatAvatar") chatAvatar: RequestBody?,
  @Part file: MultipartBody.Part?,
 ) : Response<SendRes>


}





