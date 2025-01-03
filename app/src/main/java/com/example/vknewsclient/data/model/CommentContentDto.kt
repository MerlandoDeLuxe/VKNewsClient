package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class CommentContentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("from_id") val authorId: Long,
    @SerializedName("text") val text : String,
    @SerializedName("date") val date: Long
)
