package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class CommentsDto (
    @SerializedName("count") val count: Int
)
