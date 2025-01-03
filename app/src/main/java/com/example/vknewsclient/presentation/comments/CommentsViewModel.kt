package com.example.vknewsclient.presentation.comments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
) : AndroidViewModel(application) {

    private val TAG = "CommentsViewModel"
    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState
    private var _comments = mutableListOf<PostComment>()

    private val repository = NewsFeedRepositoryImpl(application)

    init {
        Log.d(TAG, "CommentsViewModel ")
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {

            _comments = repository.getComments(feedPost).toMutableList()
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = _comments,
                nextDataIsLoading = true
            )
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = _comments,
                nextDataIsLoading = false
            )

            Log.d(TAG, "loadComments: comments = $_comments")
        }
    }
}

