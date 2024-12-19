package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.vknewsclient.domain.FeedPost
import com.google.gson.Gson

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    navigation(                     //Вложенный граф навигации для первого экрана
        startDestination = Screen.NewsFeed.route,   //здесь тот экран, который будет первым
        route = Screen.Home.route   //здесь имя вложенного графа навигации
    ) {            //Последний параметр - это builder
        composable(Screen.NewsFeed.route) {
            newsFeedScreenContent()
        }
        composable(
            route = Screen.Comments.route,
            arguments = listOf(
//                navArgument(Screen.KEY_FEED_POST) {
//                    type = NavType.StringType
//                }
                navArgument(Screen.KEY_FEED_POST) {
                    type = FeedPost.feedType
                }
            )
        ) {//comments/{feed_post_id}


//            val feedPostJson = it.arguments?.getString(Screen.KEY_FEED_POST) ?: ""
//            val feedPost = Gson().fromJson(feedPostJson, FeedPost::class.java)

            val feedPost = it.arguments?.getParcelable<FeedPost>(Screen.KEY_FEED_POST)
                ?: throw RuntimeException("Пустой аргумент")
            commentsScreenContent(feedPost)
        }
    }
}