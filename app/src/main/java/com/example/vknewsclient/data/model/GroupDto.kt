package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

data class GroupDto (
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("photo_200") val imageUrl: String
)
