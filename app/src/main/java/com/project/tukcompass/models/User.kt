package com.project.tukcompass.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var email: String,
    var phone: String,
    var profilePicUrl: String? = null // can be local path or remote URL
)


