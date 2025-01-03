package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class PhotoDto (
    @SerializedName("sizes") val photoUrls: List<PhotoUrlDto>
)
