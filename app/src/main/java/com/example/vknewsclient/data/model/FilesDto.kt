package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class FilesDto(
    @SerializedName("mp4_240") val mp4_240: String,
    @SerializedName("mp4_360") val mp4_360: String,
    @SerializedName("mp4_480") val mp4_480: String,
    @SerializedName("mp4_720") val mp4_720: String,
    @SerializedName("mp4_1080") val mp4_1080: String,
    @SerializedName("hls") val hls : String,
)
