package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class LikesDto (
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val userLikes: Int,
    @SerializedName("can_like") val canLike: Int,
    @SerializedName("can_publish") val canPublish: Int,
    @SerializedName("resposts") val resposts: RepostsDto,

    )