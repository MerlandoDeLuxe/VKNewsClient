package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(private val repository: NewsFeedRepository) {

    operator suspend fun invoke(feedPost: FeedPost) {
        return repository.deletePost(feedPost)
    }
}