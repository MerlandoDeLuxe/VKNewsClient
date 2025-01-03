package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class PhotoInStoriesDto(
    @SerializedName("album_id") val album_id: Int,
    @SerializedName("date") val date: Long,
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val owner_id: Long,
    @SerializedName("thumb_hash") val thumb_hash: String,
    @SerializedName("sizes") val sizes: List<SizeDto>,
    @SerializedName("text") val text: String,
    @SerializedName("web_view_token") val web_view_token: String,
    @SerializedName("has_tags") val has_tags: Boolean
)
