package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.Story
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoriesUseCase @Inject constructor(private val repository: NewsFeedRepository) {

    operator fun invoke(): Flow<List<Story>> {
        return repository.getStories()
    }
}