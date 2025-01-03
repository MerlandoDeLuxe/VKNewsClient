package com.example.vknewsclient.presentation.stories

import com.example.vknewsclient.domain.Story

sealed class StoriesScreenState {

    data object Initial : StoriesScreenState()

    data object Loading : StoriesScreenState()

    data class Stories(
        val stories: List<Story>? = listOf(),
        val isStoriesLoading: Boolean = false
    ) : StoriesScreenState()
}

