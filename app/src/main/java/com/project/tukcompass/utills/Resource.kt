package com.project.tukcompass.utills

 sealed class Resource<out T> {
     data class success<out T>(val data: T) : Resource<T>()
     data class error(val message: String, val errorCode: Int? = null) : Resource<Nothing>()
     object loading : Resource<Nothing>()
}
