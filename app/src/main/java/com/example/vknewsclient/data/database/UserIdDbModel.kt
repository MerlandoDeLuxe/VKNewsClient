package com.example.vknewsclient.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_id")
data class UserIdDbModel (
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    val user_id: String,
    val accessToken: String
)