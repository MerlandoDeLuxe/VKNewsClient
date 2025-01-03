package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class PostDto (
    @SerializedName("post_id") val id: Long,
    @SerializedName("source_id") val communityId: Long,
    @SerializedName("likes") val likes: LikesDto?,
    @SerializedName("text") val text: String?,
    @SerializedName("date") val date: Long,
    @SerializedName("comments") val comments: CommentsDto?,
    @SerializedName("reposts") val reposts: RepostsDto?,
    @SerializedName("attachments") val attachments: List<AttachmentDto>?
)
