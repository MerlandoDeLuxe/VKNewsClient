package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class ClickableAreaDto(
    @SerializedName("x") val x: Int,
    @SerializedName("y") val y: Int
)
