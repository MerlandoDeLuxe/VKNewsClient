package com.example.vknewsclient.domain.repository

import com.example.vknewsclient.domain.entity.AuthState
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getStories() : Flow<List<Story>>

    fun getRecomendation(): StateFlow<List<FeedPost>>

    fun getAuthentification() : StateFlow<AuthState>

    suspend fun getComments(feedPost: FeedPost): List<PostComment>

    suspend fun loadNextData()

    suspend fun deletePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)

}