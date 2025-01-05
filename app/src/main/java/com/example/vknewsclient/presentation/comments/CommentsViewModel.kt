package com.example.vknewsclient.presentation.comments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.usecase.GetCommentsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

//Инжект параметра во вью модель с использованием фабрики
@HiltViewModel(assistedFactory = CommentsViewModel.Factory::class)
class CommentsViewModel @AssistedInject constructor(
    @Assisted private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(feedPost: FeedPost) : CommentsViewModel
    }

    private val TAG = "CommentsViewModel"
    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState
    private var _comments = mutableListOf<PostComment>()


    init {
        Log.d(TAG, "CommentsViewModel ")
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {

            _comments = getCommentsUseCase(feedPost).toMutableList()
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

