package com.project.tukcompass.utills

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.project.tukcompass.models.UserModels

class EncryptedSharedPrefManager(context: Context) {

    private val gson = Gson()
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_user_session", // name of the shared pref file
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString("token", token)
        editor.apply()
    }
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }
    fun saveUser(user: UserModels) {
        val userJson = gson.toJson(user)
        editor.putString("user", userJson)
        editor.apply()
    }
    fun getUser(): UserModels? {
        val userJson = sharedPreferences.getString("user", null)
        return gson.fromJson(userJson, UserModels::class.java)
    }
    fun clear() {
        editor.clear()
        editor.apply()
    }
}