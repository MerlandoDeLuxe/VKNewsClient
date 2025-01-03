package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class RootResponseNewsFeedDto (
    @SerializedName("response") val newsFeedContent: NewsFeedContentDto
)
