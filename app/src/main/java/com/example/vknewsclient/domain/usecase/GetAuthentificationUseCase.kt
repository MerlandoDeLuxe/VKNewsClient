package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.AuthState
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthentificationUseCase @Inject constructor(private val repository: NewsFeedRepository) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthentification()
    }
}