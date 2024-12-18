package com.example.vknewsclient.ui.theme

import com.example.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {

    data object Initial : NewsFeedScreenState()    //Пустой стейт по умолчанию, хорошая практика

    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()
}