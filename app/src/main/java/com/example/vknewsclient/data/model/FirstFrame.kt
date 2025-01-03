package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class FirstFrame(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)
