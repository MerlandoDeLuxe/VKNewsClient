package com.example.vknewsclient.presentation.comments

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {

    data object Initial : CommentsScreenState()    //Пустой стейт по умолчанию, хорошая практика

    data class Comments (
        val feedPost: FeedPost,
        val comments: List<PostComment>,
        val nextDataIsLoading : Boolean = false
    ) : CommentsScreenState()
}