package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class StoriesCountItemsDto(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val objectStories: List<ObjectStoriesDto>
)
