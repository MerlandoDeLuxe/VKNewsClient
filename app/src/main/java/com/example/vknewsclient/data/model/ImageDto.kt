package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("with_padding") val with_padding: Int
)
