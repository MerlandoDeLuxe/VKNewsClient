package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRecomendationsUseCase @Inject constructor(private val repository: NewsFeedRepository) {

    operator fun invoke(): StateFlow<List<FeedPost>> {
        return repository.getRecomendation()
    }
}