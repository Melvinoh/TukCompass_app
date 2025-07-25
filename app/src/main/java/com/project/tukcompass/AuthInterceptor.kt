package com.project.tukcompass

import android.util.Log
import com.project.tukcompass.utills.EncryptedSharedPrefManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPrefManager: EncryptedSharedPrefManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = sharedPrefManager.getToken()
        Log.d(" Auth tokenLog", "$token")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
