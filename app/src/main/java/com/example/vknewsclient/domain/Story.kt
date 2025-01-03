package com.example.vknewsclient.domain

import android.net.Uri

data class Story(
    val id: Int,
    val ownerId : Int,
    val photoUrl: String,
    val type: String,
    val typeInside: String,
    val contentUri: Uri?,
    val date: String,
    val expires_at: String,
    val has_unseen: Boolean,
    val imageStoryUrl: String
)
