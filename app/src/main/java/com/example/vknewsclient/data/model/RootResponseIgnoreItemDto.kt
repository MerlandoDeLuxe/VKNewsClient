package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class RootResponseIgnoreItemDto(
    @SerializedName ("response") val ignoreItem: IgnoreItemDto
)
