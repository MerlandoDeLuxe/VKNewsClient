package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class VideoUrlsDto(
    @SerializedName("video") val files: FilesDto
)
