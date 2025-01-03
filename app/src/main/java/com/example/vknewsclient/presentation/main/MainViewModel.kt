package com.example.vknewsclient.presentation.main

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.example.vknewsclient.domain.MetaDataReader
import com.example.vknewsclient.domain.VideoItem
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val TAG = "MainViewModel"
    private val repository = NewsFeedRepositoryImpl(application)

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState
    private

    val vkAuthCallback =
        object : VKIDAuthCallback {
            override fun onAuth(accessToken: AccessToken) {
                Log.d(TAG, "onAuth: успех. accessToken =${accessToken.token}")
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    _authState.value = AuthState.Authorized
                    repository.insertAccessToken(accessToken.token, accessToken.userID.toString())
                }
            }

            override fun onFail(fail: VKIDAuthFail) {
                Log.d(TAG, "onFail: не успех ${fail.description}")
                val scope = CoroutineScope(Dispatchers.Main)
                scope.launch {
                    _authState.value = AuthState.NotAuthorized
                }
            }
        }

    init {
        val initializer = VKIDAuthParams.Builder().apply {
            scopes = setOf("status", "email", "wall", "friends", "groups", "stories", "photos")
            build()
        }
        VKIDAuthParams {
            scopes = setOf("status", "email", "wall", "friends", "groups", "stories", "photos")
        }

        viewModelScope.launch {
            VKID.instance.authorize(
                callback = vkAuthCallback,
                params = initializer.build()
            )
        }

    }



    fun onSuccess() {
        _authState.value = AuthState.Authorized
    }

    fun onFail() {
        _authState.value = AuthState.NotAuthorized
    }
}