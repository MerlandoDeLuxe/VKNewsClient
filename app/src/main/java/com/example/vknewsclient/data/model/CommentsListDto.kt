package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentsListDto(
    @SerializedName("items") val posts: List<CommentContentDto>,
    @SerializedName("profiles") val users: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>
)