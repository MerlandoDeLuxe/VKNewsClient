package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class IgnoreItemDto(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)
