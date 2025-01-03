package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class RootResponseLikesCount(
    @SerializedName("response") val likes: LikesCountDto
) {
}