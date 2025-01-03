package com.example.vknewsclient.domain

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost (
    val id: Long,
    val communityId : Long,
    val communityName: String,
    val publicationDate: String,
    val communityImageUrl: String,
    val contentText: String? = "",
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isLiked: Boolean
) : Parcelable {
    //создаем свой NavigationType для передачи целого объекта, но передача целого объекта не рекомендуется

    companion object {
        val feedType: NavType<FeedPost> = object : NavType<FeedPost>(false){
            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable(key)
            }

            override fun parseValue(value: String): FeedPost {
                return Gson().fromJson(value, FeedPost::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: FeedPost) {
                bundle.putParcelable(key, value)
            }

        }
    }
}