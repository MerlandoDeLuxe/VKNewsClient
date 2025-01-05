package com.example.vknewsclient.presentation.stories

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vknewsclient.domain.entity.Story
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
//fun Stories(ownerId: Int, listOfImages: List<Story>) {
fun Stories(stories: Map<Int, List<Story>>) {
    val TAG = "Stories"
    val pagerState = rememberPagerState(pageCount = stories.size)
    val coroutineScope = rememberCoroutineScope()

    Log.d(TAG, "Stories: $stories")

    var currentPage by remember {
        mutableStateOf(0)
    }
//    StoryImage(pagerState = pagerState, stories)

    var imageList = listOf<String>()

    stories.values.forEach { storiesList ->
        Log.d(TAG, "Stories: storiesList.size = ${storiesList.size}")
        imageList = storiesList
//            .filter {
//                it.imageStoryUrl.isNotBlank()
//            }
            .map {
                it.imageStoryUrl
            }
        Log.d(TAG, "Stories: итоговый лист list = $imageList")

        stories.values.forEachIndexed { int, value ->
            Box(
                modifier = Modifier
                    .size(150.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    // .background(brush = Brush.verticalGradient(listOf(Color.Black, Color.Transparent))),
                    horizontalArrangement = Arrangement.SpaceBetween,

                    ) {
                    Spacer(modifier = Modifier.padding(4.dp))
                    if (imageList.isNotEmpty()) {
                        StoryImage(pagerState = pagerState, imageList)
                    }

                    for (index in imageList.indices) {
                        LinearIndicator(
                            modifier = Modifier.weight(1f),
                            index == currentPage
                        ) {
                            coroutineScope.launch {

                                if (currentPage < imageList.size - 1) {
                                    currentPage++
                                }

                                pagerState.animateScrollToPage(currentPage)
                            }
                        }

                        Spacer(modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun StoryImage(pagerState: PagerState, list: List<String>) {
    val TAG = "StoryImage"
    HorizontalPager(state = pagerState, dragEnabled = true) { pagerInt ->


        var temp: String = ""
        Log.d(TAG, "StoryImage: Зашли в StoryImage")
        Log.d(TAG, "StoryImage: pagerInt = $pagerInt")
        Log.d(TAG, "StoryImage: list = $list")
//        stories.values.forEach { storiesList ->
//            Log.d(TAG, "StoryImage: stories.values.size = ${storiesList.size}")
//
//            storiesList.forEach {
//                Log.d(TAG, "StoryImage: ${it.imageStoryUrl}")
//            }
//        }

//        if (list[it].imageStoryUrl.isNotEmpty() && list[it].imageStoryUrl.isNotBlank()) {
//            temp = list[it].imageStoryUrl
//            Log.d(TAG, "StoryImage: temp = $temp")
//        }

        GlideImage(
            model = list.get(pagerInt),
            contentDescription = null,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .clickable { pagerState.currentPage + 1 }
        )
    }
}

@Composable
fun LinearIndicator(
    modifier: Modifier,
    startProgress: Boolean = false,
    onAnimationEnd: () -> Unit
) {
    var progress by remember {
        mutableStateOf(0.00f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    if (startProgress) {
        LaunchedEffect(
            key1 = Unit
        ) {
            while (progress < 1f) {
                progress += 0.01f
                delay(50)
            }

            onAnimationEnd()
        }
    }

    LinearProgressIndicator(
        progress = {
            animatedProgress
        },
        modifier = modifier
            //.background(Color.LightGray)
            .padding(top = 12.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = Color.White,
    )
}