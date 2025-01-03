package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class ObjectStoriesDto(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String,
    @SerializedName("stories") val stories: List<StoryDto>,
    @SerializedName("has_unseen") val has_unseen: Boolean,
    @SerializedName("name") val name: String,
    @SerializedName("no_author_link") val no_author_link: Boolean,
//    @SerializedName("items") val stories: List<StoryDto>
)
