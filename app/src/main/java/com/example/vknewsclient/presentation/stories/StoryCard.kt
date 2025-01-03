package com.example.vknewsclient.presentation.stories

import android.util.Log
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.Image
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.transform.RoundedCornersTransformation
import coil3.transform.Transformation
import coil3.util.CoilUtils
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vknewsclient.R
import com.example.vknewsclient.domain.Story
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.jetbrains.annotations.Async


//@OptIn(
//    ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
//    ExperimentalPagerApi::class
//)
//@Composable
//fun StoryCard(
////    onClickListener: (index: Int) -> Unit
//    paddingValues: PaddingValues,
//    stories: List<Story>
//) {
//    val TAG = "StoryCard"
//
//    Log.d(TAG, "StoryCard: Рекомпозиция 1")
//    var showDialog by remember { mutableStateOf(false) }
//    var selectedImage by remember { mutableStateOf("") }
//    val pagerState = rememberPagerState(pageCount = stories.size)
//
//
//    HorizontalMultiBrowseCarousel(
//        state = rememberCarouselState { stories.count() },
//        modifier = Modifier
//            .width(412.dp)
//            .height(250.dp),
//        preferredItemWidth = 186.dp,
//        itemSpacing = 10.dp,
//        contentPadding = PaddingValues(vertical = 50.dp)
//    ) { carouselItem ->
//
////        stories.forEach { ownerId, storiesList ->
////            Stories(storiesList)
////        }
//
//        val item = stories[carouselItem]
//        Log.d(TAG, "StoryCard: itemUrl : $item")
//        GlideImage(
//            model = item.imageStoryUrl,
//            modifier = Modifier
////                    .padding(50.dp)
//
////                .wrapContentHeight()
////                .height(205.dp)
//                .maskClip(MaterialTheme.shapes.extraLarge)
//                .clickable {
//                    showDialog = true
//                    selectedImage = item.imageStoryUrl
//                    Log.d(TAG, "StoryCard: clickable вызвался ")
//                },
//            contentDescription = "",
//            contentScale = ContentScale.Crop
//        )
//
//    }
//
//    if (showDialog) {
//        Dialog(
//            onDismissRequest = { showDialog = false }
//        ) {
//            HorizontalPager(
//                state = pagerState,
//                dragEnabled = true,
//            ) { page ->
//                Log.d(TAG, "StoryCard: Рекомпозиция Dialog ")
//                GlideImage(
//                    model = selectedImage,
//                    modifier = Modifier
//                        .wrapContentSize(),
//                    contentDescription = ""
//                )
//
//                    Log.d(TAG, "StoryCard: imageStoryUrl = ${stories.get(page).imageStoryUrl}")
//                    selectedImage = stories.get(page).imageStoryUrl
//                }
//
//        }
//    }
//}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
    ExperimentalPagerApi::class
)
@Composable
fun StoryCard(
//    onClickListener: (index: Int) -> Unit
    paddingValues: PaddingValues,
    stories: List<Story>
) {
    val TAG = "StoryCard"

    Log.d(TAG, "StoryCard: Рекомпозиция 1")
    val isFullScreen by remember { mutableStateOf(false) }
    val currentPhotoIndex by remember { mutableStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf("") }
    val pagerState = rememberPagerState(pageCount = stories.size)


    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { stories.count() },
        modifier = Modifier
            .width(412.dp)
            .height(250.dp)
            .padding(top = 40.dp)
        ,
        preferredItemWidth = 186.dp,
        itemSpacing = 10.dp,
//        contentPadding = paddingValues
    ) { carouselItem ->

        val item = stories[carouselItem]
        Log.d(TAG, "StoryCard: itemUrl : $item")
        GlideImage(
            model = item.imageStoryUrl,
            modifier = Modifier
//                    .padding(50.dp)

//                .wrapContentHeight()
//                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge)
                .clickable {
                    showDialog = true
                    selectedImage = item.imageStoryUrl
                    Log.d(TAG, "StoryCard: clickable вызвался ")
                },
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            HorizontalPager(
                state = pagerState,
                dragEnabled = true,
            ) { page ->
                Log.d(TAG, "StoryCard: Рекомпозиция Dialog ")

//                selectedImage = stories.get(page).imageStoryUrl

                Log.d(TAG, "StoryCard: imageStoryUrl = ${stories.get(page).imageStoryUrl}")
//Бесконечная рекомпозиция если один раз свайпнуть влево или вправо. как починить пока хз
                FullScreenImage(
                    selectedImage = selectedImage,
                    onDismiss = {
                    },
                    onNext = {
                        pagerState.currentPage + 1
                        selectedImage = stories.get(page).imageStoryUrl
                        OpenImage(selectedImage)
                    },
                    onPrevious = {
                        pagerState.currentPage - 1
                        selectedImage = stories.get(page).imageStoryUrl
                        OpenImage(selectedImage)
                    },
                )

            }
        }
    }
}

@Composable
fun FullScreenImage(
    selectedImage: String,
    onDismiss: () -> Unit,
    onNext: @Composable () -> Unit,
    onPrevious: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState()
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {}
    ) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
            onPrevious()
        }
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onNext()
        }
        OpenImage(selectedImage)
    }
//    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OpenImage(
    selectedImage: String
) {
    GlideImage(
        model = selectedImage,
        modifier = Modifier
            .wrapContentSize(),
        contentDescription = ""
    )
}