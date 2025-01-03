package com.example.vknewsclient.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.database.UserIdDbModel
import com.example.vknewsclient.data.database.VkDatabase
import com.example.vknewsclient.data.mapper.NewsFeedMapper
import com.example.vknewsclient.data.model.RootResponseLikesCount
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.example.vknewsclient.domain.Story
import com.example.vknewsclient.extension.mergeWith
import com.example.vknewsclient.presentation.stories.StoriesScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class NewsFeedRepositoryImpl(private val application: Application) {

    private val TAG = "NewsFeedRepositoryImpl"

    private val connectDb = VkDatabase.getInstance(application).dao()
    private val apiFactory = ApiFactory.apiService
    private val scope = CoroutineScope(Dispatchers.IO)
    private var token = ""
    private val mapper = NewsFeedMapper()

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom //Добавили новую переменную ненулабельную
            if (startFrom == null && feedPosts.isNotEmpty()) { //Чтобы не дублировались
                emit(feedPosts)
                return@collect
            }
            // данные если мы получили уже все рекомендуемые данные и API вернула пустой список
            val userFromDb = scope.async {
                getUserFromDbWithAccessToken()
            }
            val userIdDbModel = userFromDb.await()
            val result = scope.async {
                val response = if (startFrom == null) {
                    apiFactory.loadNewsFeed(userIdDbModel.accessToken)
                } else {
                    apiFactory.loadNewsFeedNext(startFrom, userIdDbModel.accessToken)
                }
                delay(500)
                nextFrom = response.newsFeedContent.nextFrom

                val posts = mapper.mapResponseToPost(response)
                _feedPosts.addAll(posts)
                _feedPosts
            }
            result.await()
            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        nextDataNeededEvents.emit(Unit)
        true
    }

    val getStories = flow {
        val userIdDbModel = getUserFromDbWithAccessToken()

        val user = apiFactory.getUser(
            userId = userIdDbModel.user_id,
            token = userIdDbModel.accessToken
        )
        Log.d(TAG, "getStories: user = $user")

        val stories = apiFactory.getStories(
            //  ownerId = userIdDbModel.user_id.toLong(),
            token = userIdDbModel.accessToken,
        )

        Log.d(TAG, "getStories: stories = $stories")

        val resultStoriesList = mapper.mapStoryDtoToStory(stories)
        for (i in resultStoriesList) {
            Log.d(TAG, "getStories: $i")
        }
        emit(resultStoriesList)
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private var _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() {
            return _feedPosts.toList()
        }

    fun insertAccessToken(accessToken: String, userId: String) {
        scope.launch {
            val userIdDbModel = UserIdDbModel(user_id = userId, accessToken = accessToken)
            connectDb.insertUserData(userIdDbModel)
        }
    }

    suspend fun getUserFromDbWithAccessToken(): UserIdDbModel {
        val result = scope.async {
            delay(2000)
            val userIdDbModel = connectDb.getAccessToken(1)
            token = userIdDbModel.accessToken
            Log.d(TAG, "getAccessToken: userIdDbModel = ${userIdDbModel}")
            userIdDbModel
        }

        return result.await()
    }

    private var nextFrom: String? = null

    val recommendations = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiFactory.ignorePost(
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            token = token,
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        Log.d(TAG, "changeLikeStatus: ${feedPost.id}")
        Log.d(TAG, "changeLikeStatus: ${feedPost.communityId}")
        Log.d(TAG, "changeLikeStatus: ${feedPost.communityName}")
        Log.d(
            TAG, "changeLikeStatus: ${
                feedPost.statistics.filter {
                    it.type == StatisticType.LIKES
                }
                    .forEach {
                        Log.d(TAG, "changeLikeStatus: ${it.count}")

                    }
            }"
        )
        val response: RootResponseLikesCount
        feedPost.isLiked
        response = if (feedPost.isLiked) {
            apiFactory.deleteLike(
                ownerId = feedPost.communityId,
                postId = feedPost.id,
                token = token
            )
        } else {
            apiFactory.addLike(
                ownerId = feedPost.communityId,
                postId = feedPost.id,
                token = token
            )
        }

        val newLikesCount = response.likes.count

        Log.d(TAG, "changeLikeStatus: newLikesCount = $newLikesCount")

//        val oldStatistics = feedPost.statistics
//        val newStatistics = oldStatistics.map {
//            if (it.type == StatisticType.LIKES) {
//                it.count = newLikesCount
//            }
//            it
//        }
//
//        Log.d(TAG, "addLike: oldFeedPostStatistics = $oldStatistics")
//        Log.d(TAG, "addLike: newFeedPostStatistics = $newStatistics")

        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf {
                it.type == StatisticType.LIKES  //Удаляем из коллекции элемент, который хранит кол-во лайков
            }
            add(
                StatisticItem(
                    type = StatisticType.LIKES,
                    count = newLikesCount
                )
            ) //Вставляем туда новый
        }

        Log.d(TAG, "changeLikeStatus: newStatistics = $newStatistics")
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = feedPosts.indexOf(feedPost) //Заменяем старый объект на новый по индексу

        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun getComments(feedPost: FeedPost): List<PostComment> {
        val userIdDbModel = getUserFromDbWithAccessToken()
        val response = apiFactory.getComments(
            postId = feedPost.id,
            ownerId = feedPost.communityId,
            token = userIdDbModel.accessToken
        )
        Log.d(TAG, "getComments: response = $response")
        return mapper.mapResponseToComments(response)
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}