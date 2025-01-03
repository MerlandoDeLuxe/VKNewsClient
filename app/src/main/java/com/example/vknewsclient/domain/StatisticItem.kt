package com.example.vknewsclient.domain

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.vknewsclient.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticItem (
    val type: StatisticType,
    var count: Int? = 0
) : Parcelable

enum class StatisticType(
    @DrawableRes
    val iconResId: Int
) {
    VIEWS(R.drawable.view_eye_light_theme),
    SHARES(R.drawable.repost_light_theme),
    COMMENTS(R.drawable.comment_light_theme),
    LIKES(R.drawable.like_set)
}