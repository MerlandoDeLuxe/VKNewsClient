package com.example.vknewsclient.presentation.stories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vknewsclient.data.repository.NewsFeedRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "StoriesViewModel"
    private val repository = NewsFeedRepositoryImpl(application)

    val stories = repository.getStories
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