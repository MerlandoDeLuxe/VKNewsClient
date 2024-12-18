package com.example.vknewsclient.domain

import com.example.vknewsclient.R

data class FeedPost(
    val id: Int = 0,
    val communityName: String = "Астрономия",
    val publicationDate: String = "14:00",
    val avatarResId: Int = R.drawable.avatar,
    val contentText: String = "Съешь еще этих мягких французских булок да выпей чаю",
    val contentImageResId: Int = R.drawable.avatar,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 474),
        StatisticItem(StatisticType.SHARES, 13),
        StatisticItem(StatisticType.COMMENTS, 16),
        StatisticItem(StatisticType.LIKES, 33)
    )
)