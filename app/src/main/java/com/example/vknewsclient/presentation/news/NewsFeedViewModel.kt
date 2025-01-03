package com.example.vknewsclient.presentation.news

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.MetaDataReader
import com.example.vknewsclient.domain.VideoItem
import com.example.vknewsclient.extension.mergeWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    application: Application,
    val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader
) : AndroidViewModel(application) {
    private val TAG = "NewsFeedViewModel"

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d(TAG, "Исключение поймано exceptionHandler") }
    private val repository = NewsFeedRepositoryImpl(application)

    private val recommendationFlow = repository.recommendations

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    val screenState = recommendationFlow
        .filter {
            it.isNotEmpty()
        }
        .map {
            NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState
        }
        .onStart {
            emit(NewsFeedScreenState.Loading)
        }
        .mergeWith(loadNextDataFlow)

    val videoUris = savedStateHandle.getStateFlow("video", listOf<Uri>())

    val videoItems = videoUris.map { uris ->
        uris.map { uri ->
            Log.d(TAG, "uri = : $uri")
            VideoItem(
                contentUri = uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name"
            )

        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


//    fun addVideoItem() {
//        Log.d(TAG, "addVideoItem: _listOfStories : $_listOfStories")
//        for (story in _listOfStories) {
//
//            Log.d(TAG, "addVideoItem: story = $story")
//
//            savedStateHandle["videoUris"] = videoUris.value + story
//            player.addMediaItem(MediaItem.fromUri(story))
//        }
//    }

    fun playVideo(uri: Uri) {
        player.setMediaItem(
            videoItems.value.find {
                it.contentUri == uri
            }?.mediaItem ?: return
        )
    }

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationFlow.value,
                    nextDataIsLoading = true
                )
            )

            repository.loadNextData()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler) {
            repository.changeLikeStatus(feedPost)
        }
    }

    fun removeItem(feedPost: FeedPost) {
        viewModelScope.launch (exceptionHandler) {
            repository.deletePost(feedPost)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
