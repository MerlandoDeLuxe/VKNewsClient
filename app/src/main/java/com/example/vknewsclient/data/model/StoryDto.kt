package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class StoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val owner_id: Int,
    @SerializedName("access_key") val access_key: String,
    @SerializedName("can_comment") val can_comment: Int,
    @SerializedName("can_reply") val can_reply: Int,
    @SerializedName("can_see") val can_see: Int,
    @SerializedName("can_like") val can_like: Boolean,
    @SerializedName("can_share") val can_share: Int,
    @SerializedName("can_hide") val can_hide: Int,
    @SerializedName("date") val date: Long,
    @SerializedName("expires_at") val expires_at: Long,
    @SerializedName("track_code") val track_code: String,
    @SerializedName("type") val type: String,
    @SerializedName("video") val video: VideoDto?,
    @SerializedName("reaction_set_id") val reaction_set_id: String,
    @SerializedName("is_restricted") val is_restricted: Boolean,
    @SerializedName("no_sound") val no_sound: Boolean,
    @SerializedName("can_ask") val can_ask: Int,
    @SerializedName("can_ask_anonymous") val can_ask_anonymous: Int,
    @SerializedName("photo") val photo: PhotoDto?,
    @SerializedName("clickable_stickers") val clickable_stickers: ClickableStickerDto,
    @SerializedName("link") val link: LinkDto,
)
