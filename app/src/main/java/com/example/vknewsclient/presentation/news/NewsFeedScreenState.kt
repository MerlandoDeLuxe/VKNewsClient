package com.example.vknewsclient.presentation.news

import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.Story
import javax.inject.Inject

sealed class NewsFeedScreenState {

    data object Initial : NewsFeedScreenState()   //Пустой стейт по умолчанию, хорошая практика

    data object Loading : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()

}