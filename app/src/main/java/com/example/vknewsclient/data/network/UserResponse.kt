package com.example.vknewsclient.data.network

import com.example.vknewsclient.data.model.NewsFeedContentDto
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("response") val users: List<User>
) {
}