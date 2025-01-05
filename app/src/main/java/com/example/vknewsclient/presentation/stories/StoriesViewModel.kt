package com.example.vknewsclient.presentation.stories

import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.usecase.GetStoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    getStoriesUseCase : GetStoriesUseCase
) : ViewModel() {

    private val TAG = "StoriesViewModel"


    val stories = getStoriesUseCase()
        .filter {
            it.isNotEmpty()
        }
        .map {
            (StoriesScreenState.Stories(stories = it) as StoriesScreenState)
        }
        .onStart {
            StoriesScreenState.Loading
        }
}