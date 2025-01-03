package com.example.vknewsclient.presentation.stories

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vknewsclient.domain.Story

@Composable
fun StoriesScreen(
    paddingValues: PaddingValues,
) {

    val TAG = "StoriesScreen"

    val viewModel: StoriesViewModel = hiltViewModel()
    val storiesState = viewModel.stories.collectAsState(StoriesScreenState.Initial)

    Log.d(TAG, "StoriesScreen: Рекомпозиция 1 ${storiesState.value}")

    when (val currentState = storiesState.value) {
        is StoriesScreenState.Initial -> {}
        is StoriesScreenState.Loading -> {}
        is StoriesScreenState.Stories -> {

            StoryCard(
                paddingValues = paddingValues,
                stories = currentState.stories ?: listOf()
            )
//            currentState.stories?.forEach{
//                Stories(it.key, it.value)
//            }
        }
    }
}
