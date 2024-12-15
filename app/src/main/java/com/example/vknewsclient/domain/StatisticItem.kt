package com.example.vknewsclient.domain

import androidx.annotation.DrawableRes
import com.example.vknewsclient.R

data class StatisticItem(
    val type: StatisticType,
    val count: Int
)

enum class StatisticType(
    @DrawableRes
    val iconResId: Int
) {
    VIEWS(R.drawable.view_eye_light_theme),
    SHARES(R.drawable.repost_light_theme),
    COMMENTS(R.drawable.comment_light_theme),
    LIKES(R.drawable.like_light_theme)
}