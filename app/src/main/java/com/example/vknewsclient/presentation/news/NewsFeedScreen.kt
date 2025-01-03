package com.example.vknewsclient.presentation.news

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.presentation.stories.StoriesScreen

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val TAG = "NewsFeedScreen"
    val viewModel: NewsFeedViewModel = hiltViewModel()
    val screenState =
        viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)


    Log.d(TAG, "NewsFeedScreen: Рекомпозиция 1 ${screenState.value}")

    when (val currentState = screenState.value) {
        NewsFeedScreenState.Initial -> {}

        is NewsFeedScreenState.Posts -> {
            Log.d(TAG, "NewsFeedScreen: ")
            Column() {

                FeedPosts(
                    viewModel = viewModel,
                    posts = currentState.posts,
                    paddingValues = paddingValues,
                    onCommentClickListener = onCommentClickListener,
                    nextDataIsLoading = currentState.nextDataIsLoading
                )
            }

        }

        NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    viewModel: NewsFeedViewModel,
    posts: List<FeedPost>,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    nextDataIsLoading: Boolean
) {
    val TAG = "NewsFeedScreen"

    Log.d(TAG, "NewsFeedScreen: Рекомпозиция 2")

    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
//            .padding(paddingValues)
            .padding(start = 8.dp, end = 8.dp),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {  //Вызов в этом блоке списка историй даёт возможность при вертикальном скролле опускаться вниз, скрывая их
            StoriesScreen(paddingValues)

            Spacer(modifier = Modifier.height(15.dp))
            Spacer(modifier = Modifier.padding(4.dp))
        }
        items(
            items = posts,
            key = { it.id }
        )
        { feedPost ->

            Log.d(TAG, "FeedPosts: $feedPost")
            val dismissState = rememberSwipeToDismissBoxState()
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {},
                modifier = Modifier
                    .animateItemPlacement(),
                enableDismissFromStartToEnd = false,
            ) {
                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                    viewModel.removeItem(feedPost)
                }
                PostCard(
                    modifier = Modifier,
                    feedPost = feedPost,
                    onCommentClickListener = {
                        onCommentClickListener(feedPost)
                    },
                    onLikeClickListener = { _ ->
                        viewModel.changeLikeStatus(feedPost)
                    }
                )
            }
        }
        //Эта функция вызовется, когда мы доскролим до конца и выполнится код внутри нее
        item {
            //Произойдет проверка, что следующие данные не находятся в состоянии загрузки
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    CircularProgressIndicator(color = Color.Gray)
                }
            } else {
                //Поэтому мы её запустим
                SideEffect {
                    viewModel.loadNextRecommendations()
                }
            }
        }
    }
}