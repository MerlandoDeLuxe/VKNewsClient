package com.example.vknewsclient.navigation

import android.net.Uri
import com.example.vknewsclient.domain.FeedPost
import com.google.gson.Gson

sealed class Screen(
    val route: String //В библиотеке Jetpack Compose Navigation навигация идет через названия
) {
    data object NewsFeed : Screen(ROUTE_NEWS_FEED)
    data object Favorite : Screen(ROUTE_FAVORITE)
    data object Profile : Screen(ROUTE_PROFILE)
    data object Home : Screen(ROUTE_HOME)

    data object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_WITH_ARGS = "comments"

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            return "$ROUTE_WITH_ARGS/${feedPostJson.encode()}"
        }
    }

    companion object {
        const val KEY_FEED_POST = "feed_post"

        const val ROUTE_HOME = "home"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVORITE = "route_favorite"
        const val ROUTE_PROFILE = "route_profile"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}
