package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class PhotoUrlDto (
    @SerializedName("url") val url: String
)