package com.example.vknewsclient.domain

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.vknewsclient.R
import com.example.vknewsclient.navigation.NavigationState
import com.example.vknewsclient.navigation.Screen
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Int = 0,
    val communityName: String = "Астрономия",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.avatar,//Съешь еще этих мягких французских булок
    val contentText: String = "да выпей чаю",
    val contentImageResId: Int = R.drawable.avatar,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 474),
        StatisticItem(StatisticType.SHARES, 13),
        StatisticItem(StatisticType.COMMENTS, 16),
        StatisticItem(StatisticType.LIKES, 33)
    )
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