package com.example.vknewsclient.domain

import com.example.vknewsclient.R

data class PostComment(
    val id: Int,
    val authorName: String = "Author",
    val authorAvatarId: Int = R.drawable.man_user_color_icon,
    val commentText: String = "Текст комментария по умолчанию",
    val publicationDate: String = "14:00"
)
