package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class NewsFeedContentDto (
    @SerializedName("items") val posts: List<PostDto>,
    @SerializedName("groups") val groups: List<GroupDto>,
    @SerializedName("next_from") val nextFrom : String?
)
