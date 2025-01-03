package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName


data class ClickableStickerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("clickable_area") val clickable_area: List<ClickableAreaDto>,
    @SerializedName("sticker_id") val sticker_id: Int,
    @SerializedName("sticker_pack_id") val sticker_pack_id: Int,
    @SerializedName("original_height") val original_height: Int,
    @SerializedName("original_width") val original_width: Int,
    @SerializedName("clickable_stickers") val clickable_stickers: List<ClickableStickerDto>
)


