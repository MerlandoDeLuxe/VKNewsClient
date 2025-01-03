package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class RootResponseStories(
    @SerializedName("response") val storiesResponse: StoriesCountItemsDto
)
