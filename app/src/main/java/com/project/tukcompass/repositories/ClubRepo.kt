package com.project.tukcompass.repositories

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.project.tukcompass.Api
import com.project.tukcompass.models.CommentReqData
import com.project.tukcompass.models.CommentRequest
import com.project.tukcompass.models.CommentResponse
import com.project.tukcompass.models.PostResponse
import com.project.tukcompass.utills.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ClubRepo @Inject constructor(private val api: Api) {
    suspend fun getPosts(id: String) : Resource<PostResponse> {

        return try {
            val response = api.getPosts(id)

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

    suspend fun createPost(description: String, clubID: String, imageUri: Uri?, context: Context): Resource<PostResponse> {
        return try {
            val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart = imageUri?.let {
                val file = File(getRealPathFromUri(context, it))
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
            val response = api.createPost(descriptionPart, imagePart, clubID )
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Failed with code ${response.code()}: ${response.message()}")
            }

        } catch (e: IOException) {
            Log.e("CreatePostError", "IO: ${e.localizedMessage}")
            Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("CreatePostError", "Unknown: ${e.localizedMessage}")
            Resource.Error("Unexpected Error: ${e.localizedMessage}")
        }
    }

    suspend fun getComments( postID: CommentRequest): Resource<CommentResponse>{

        return try {
            val response = api.getComments(postID)
            if (response.isSuccessful) {


                val body = response.body()

                Log.d("ClubAPI", "Response Body: ${response.body()}")
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

    suspend fun addComments( reqBody: CommentReqData): Resource<CommentResponse>{
        return try {
            val response = api.addComment(reqBody)
            if (response.isSuccessful) {
                val body = response.body()

                Log.d("ClubAPI", "Response Body: ${response.body()}")
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





    private fun getRealPathFromUri(context: Context, uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path ?: ""
    }

}