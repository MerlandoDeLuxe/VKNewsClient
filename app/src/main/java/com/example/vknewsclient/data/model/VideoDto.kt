package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName
import java.nio.file.Files

data class VideoDto(
    @SerializedName("files") val files: FilesDto?,
    @SerializedName("response_type") val response_type: String,
    @SerializedName("access_key") val access_key: String,
    @SerializedName("can_add") val can_add: Int,
    @SerializedName("can_play_in_background") val can_play_in_background: Int,
    @SerializedName("can_download") val can_download: Int,
    @SerializedName("date") val date: Int,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("image") val imageDto: List<ImageDto>,
    @SerializedName("first_frame") val first_frame: List<FirstFrame>,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val owner_id: Long,
    @SerializedName("ov_id") val ov_id: String,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("views") val views: Int,
    @SerializedName("can_dislike") val can_dislike: Int,
    @SerializedName("thumb_hash") val thumb_hash: String
)
