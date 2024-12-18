package com.example.vknewsclient.ui.theme

import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment

sealed class CommentsScreenState {

    data object Initial : CommentsScreenState()    //Пустой стейт по умолчанию, хорошая практика

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}