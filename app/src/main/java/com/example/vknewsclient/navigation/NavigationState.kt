package com.example.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vknewsclient.domain.FeedPost

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String) {
        navHostController.navigate(route) {

            //Чтобы в стеке хранился только стартовый экран
            popUpTo(navHostController.graph.findStartDestination().id) {//Берем стартовый экран из контроллера
                saveState = true    //При удалении экранов из бекстека у них будет сохранён стейт
            }
            launchSingleTop = true  //Чтобы один экран лежал в бекстеке,
            restoreState = true //Восстановить стейт, который сохранили при удалении из бекстека
        }
    }

    fun navigateToComments(feedPost: FeedPost) {
        navHostController.navigate(Screen.Comments.getRouteWithArgs(feedPost))   //comments/15/name_of_community/...
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}