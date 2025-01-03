package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class SizeDto(
    @SerializedName("height") val height: Int,
    @SerializedName("type") val type: String,
    @SerializedName("width") val width: Int,
    @SerializedName("url") val url: String
)
