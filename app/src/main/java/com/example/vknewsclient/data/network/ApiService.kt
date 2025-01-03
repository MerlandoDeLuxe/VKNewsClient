package com.example.vknewsclient.data.network

import com.example.vknewsclient.data.model.RootResponseCommentsDto
import com.example.vknewsclient.data.model.RootResponseIgnoreItemDto
import com.example.vknewsclient.data.model.RootResponseLikesCount
import com.example.vknewsclient.data.model.RootResponseNewsFeedDto
import com.example.vknewsclient.data.model.RootResponseStories
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //getRecommended
    @GET("newsfeed.getRecommended")
    suspend fun loadNewsFeed(
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseNewsFeedDto

    @GET("newsfeed.getRecommended")
    suspend fun loadNewsFeedNext(
        @Query(START_FROM) startFrom: String,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseNewsFeedDto

    @GET("account.getAppPermissions")
    suspend fun getAppPermission(
        @Query(USER_ID) userId: String,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): Int

    @GET("stories.get")
    suspend fun getStories(
     //   @Query(OWNER_ID) ownerId: Long,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseStories

    @POST("users.get")
    suspend fun getUser(
        @Query(USER_ID) userId: String,
        @Query(FIELDS) fieldsValue: String = FIELDS_VALUE,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): UserResponse

    @GET("likes.add?type=post")
    suspend fun addLike(
        @Query(OWNER_ID) ownerId: Long,
        @Query(POST_ID) postId: Long,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseLikesCount

    @GET("likes.delete?type=post")
    suspend fun deleteLike(
        @Query(OWNER_ID) ownerId: Long,
        @Query(POST_ID) postId: Long,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseLikesCount

    @GET("newsfeed.ignoreItem")
    suspend fun ignorePost(
        @Query(TYPE) type: String = WALL,
        @Query(OWNER_ID) ownerId: Long,
        @Query(POST_ID) postId: Long,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseIgnoreItemDto

    @GET("newsfeed.getComments")
    suspend fun getComments(
        @Query(FILTERS) filters: String = FILTER_TYPE_POST,
//        @Query(REPOSTS) postId: String,
        @Query("post_id") postId: Long,
        @Query(OWNER_ID) ownerId: Long,
//        @Query("count") postCount: Int = 30,
        @Query(EXTENDED) withCommentsAuthors: Int = 1,
        @Query(FIELDS) photoSize: String = PHOTO_SIZE,
//        @Query("last_comments") lastComments : Int = 1,
//        @Query("last_comments_count") lastCommentsCount: Int = 10,
        @Query(ACCEESS_TOKEN) token: String,
        @Query(VERSION) version: String = VERSION_NUM
    ): RootResponseCommentsDto

    companion object {
        private const val PHOTO_SIZE = "photo_100"
        private const val EXTENDED = "extended"
        private const val REPOSTS = "reposts"
        private const val FILTER_TYPE_POST = "post"
        private const val FILTERS = "filters"
        private const val WALL = "wall"
        private const val TYPE = "type"
        private const val START_FROM = "start_from"
        private const val OWNER_ID = "owner_id"
        private const val POST_ID = "item_id"
        private const val FIELDS = "fields"
        private const val FIELDS_VALUE = "bdate"
        private const val USER_ID = "user_id"
        private const val VERSION = "v"
        private const val VERSION_NUM = "5.199"
        private const val ACCEESS_TOKEN = "access_token"
    }

}