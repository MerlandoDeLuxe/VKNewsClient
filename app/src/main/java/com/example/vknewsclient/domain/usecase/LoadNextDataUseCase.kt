package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.repository.NewsFeedRepository
import javax.inject.Inject

class LoadNextDataUseCase @Inject constructor(private val repository: NewsFeedRepository) {

    operator suspend fun invoke() {
        return repository.loadNextData()
    }
}