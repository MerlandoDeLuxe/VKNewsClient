package com.example.vknewsclient.domain.entity

import javax.inject.Inject

data class PostComment @Inject constructor (
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationDate: String
)
