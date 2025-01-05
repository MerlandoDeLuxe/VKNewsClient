package com.example.vknewsclient.data

import android.app.Application
import android.util.Log
import com.example.vknewsclient.data.database.UserIdDbModel
import com.example.vknewsclient.data.database.VkDatabase
import com.example.vknewsclient.data.mapper.NewsFeedMapper
import com.example.vknewsclient.data.model.RootResponseLikesCount
import com.example.vknewsclient.data.network.ApiFactory
import com.example.vknewsclient.data.network.ApiService
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.StatisticType
import com.example.vknewsclient.extension.mergeWith
import com.example.vknewsclient.domain.entity.AuthState
import com.example.vknewsclient.domain.entity.Story
import com.example.vknewsclient.domain.repository.NewsFeedRepository
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val connect : VkDatabase,
    private val mapper : NewsFeedMapper,
    private val apiFactory : ApiService
) : NewsFeedRepository {

    private val TAG = "NewsFeedRepositoryImpl"

    private val connectDb = connect.dao()

    private val scope = CoroutineScope(Dispatchers.IO)
    private var token = ""

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

            emit(feedPosts)
            Log.d(TAG, "feedPost = $feedPosts ")
            Log.d(TAG, "_feedPosts: $_feedPosts")
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        nextDataNeededEvents.emit(Unit)
        true
    }

    private val getStories = flow {
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

    private val recommendations = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    private val authState = MutableStateFlow<AuthState>(AuthState.Initial)

    private val authentification = flow {

        val initializer = VKIDAuthParams.Builder().apply {
            scopes =
                setOf("status", "email", "wall", "friends", "groups", "stories", "photos")
            build()
        }

        val vkAuthCallback =
            object : VKIDAuthCallback {
                override fun onAuth(accessToken: AccessToken) {
                    Log.d(TAG, "onAuth: Успех")
                    authState.value = AuthState.Authorized
                    insertAccessToken(
                        accessToken = accessToken.token,
                        userId = accessToken.userID.toString()
                    )
                }

                override fun onFail(fail: VKIDAuthFail) {
                    Log.d(TAG, "onFail: не успех ${fail.description}")
                    authState.value = AuthState.NotAuthorized
                }
            }

        authState.collect {
            VKID.instance.authorize(
                callback = vkAuthCallback,
                params = initializer.build()
            )
            emit(it)
            Log.d(TAG, "it AuthState = ${it}")
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun deletePost(feedPost: FeedPost) {
        apiFactory.ignorePost(
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            token = token,
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        Log.d(TAG, "changeLikeStatus feedPost.id: ${feedPost.id}")
        Log.d(TAG, "changeLikeStatus feedPost.communityId: ${feedPost.communityId}")
        Log.d(TAG, "changeLikeStatus feedPost.communityName: ${feedPost.communityName}")

        val userIdDbModel = getUserFromDbWithAccessToken()
        Log.d(TAG, "changeLikeStatus: userIdDbModel = $userIdDbModel")

        val response: RootResponseLikesCount

        response = if (feedPost.isLiked) {
            Log.d(TAG, "changeLikeStatus: пост был пролайкан, лайк нужно снять")
            apiFactory.deleteLike(
                ownerId = feedPost.communityId,
                postId = feedPost.id,
                token = userIdDbModel.accessToken
            )
        } else {
            Log.d(TAG, "changeLikeStatus: пост не был пролайкан, лайк нужно поставить")
            apiFactory.addLike(
                ownerId = feedPost.communityId,
                postId = feedPost.id,
                token = userIdDbModel.accessToken
            )
        }

        Log.d(TAG, "changeLikeStatus: response = $response")
        val newLikesCount = response.likes.count

        Log.d(TAG, "changeLikeStatus: newLikesCount = $newLikesCount")

        val oldStatistics = feedPost.statistics
        val newStatistics = oldStatistics.map {
            if (it.type == StatisticType.LIKES) {
                it.count = newLikesCount
            }
            it
        }

        Log.d(TAG, "changeLikeStatus: oldFeedPostStatistics = $oldStatistics")
        Log.d(TAG, "changeLikeStatus: newFeedPostStatistics = $newStatistics")

//        val newStatistics = feedPost.statistics.toMutableList().apply {
//            removeIf {
//                it.type == StatisticType.LIKES  //Удаляем из коллекции элемент, который хранит кол-во лайков
//            }
//            add(
//                StatisticItem(
//                    type = StatisticType.LIKES,
//                    count = newLikesCount
//                )
//            ) //Вставляем туда новый
//        }

        Log.d(TAG, "changeLikeStatus: newStatistics = $newStatistics")
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = feedPosts.indexOf(feedPost) //Заменяем старый объект на новый по индексу

        _feedPosts[postIndex] = newPost
        refreshedListFlow.emit(feedPosts)
    }

    override fun getStories(): Flow<List<Story>> = getStories

    override fun getRecomendation(): StateFlow<List<FeedPost>> = recommendations

    override fun getAuthentification(): StateFlow<AuthState> = authentification

    override suspend fun getComments(feedPost: FeedPost): List<PostComment> {
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