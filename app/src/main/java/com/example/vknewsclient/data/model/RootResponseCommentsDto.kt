package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class RootResponseCommentsDto(
    @SerializedName("response") val content: CommentsListDto
)
