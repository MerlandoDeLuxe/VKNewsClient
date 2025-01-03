package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class LinkDto(
    @SerializedName("text") val text: String,
    @SerializedName("url") val url: String
)
