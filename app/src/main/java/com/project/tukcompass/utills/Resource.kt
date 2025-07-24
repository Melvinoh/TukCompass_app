package com.project.tukcompass.utills

 sealed class Resource<out T> {
     data class Success<out T>(val data: T) : Resource<T>()
     data class Error(val message: String, val errorCode: Int? = null) : Resource<Nothing>()
     object Loading : Resource<Nothing>()
}
