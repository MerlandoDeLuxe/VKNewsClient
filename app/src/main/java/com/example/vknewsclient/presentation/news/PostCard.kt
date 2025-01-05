package com.example.vknewsclient.presentation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.vknewsclient.R
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
import com.example.vknewsclient.ui.theme.DarkRed

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLikeClickListener: (StatisticItem) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            feedPost.contentText?.let {
                Text(
                    text = it,
                    fontStyle = FontStyle.Normal,
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SetImageNews(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            SetStatisticsBar(
                statistics = feedPost.statistics,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
                isLiked = feedPost.isLiked
            )
        }
    }
}

@Composable
private fun PostHeader(
    feedPost: FeedPost
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SetGroupAvatar(feedPost)
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = feedPost.communityName,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feedPost.publicationDate,
                color = Color.Gray
            )
        }

        SetMoreButton()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SetGroupAvatar(
    feedPost: FeedPost
) {
    GlideImage(
        model = feedPost.communityImageUrl,
        modifier = Modifier
//            .padding(10.dp)
            .clip(CircleShape)
            .background(Color.White)
            .size(50.dp),
        contentDescription = "",
        contentScale = ContentScale.Fit
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SetImageNews(
    feedPost: FeedPost
) {
    GlideImage(
        model = feedPost.contentImageUrl,
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        // .border(0.5.dp, Color.Gray),
        contentScale = ContentScale.FillWidth   //Растягивает картинку сохраняя соотношение сторон
    )
}

@Composable
private fun SetStatisticsBar(
    statistics: List<StatisticItem>,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLikeClickListener: (StatisticItem) -> Unit,
    isLiked: Boolean
) {
    Row {
        Row(modifier = Modifier.weight(1f)) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            val countViewsItem = viewsItem.count ?: 0
            IconWithText(
                iconResId = viewsItem.type.iconResId,
                text = formatStatisticCount(countViewsItem)
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            val countSharesItem = sharesItem.count ?: 0
            IconWithText(
                iconResId = sharesItem.type.iconResId,
                text = formatStatisticCount(countSharesItem)
            )

            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            val countCommentsItem = commentsItem.count ?: 0
            IconWithText(
                iconResId = commentsItem.type.iconResId,
                text = formatStatisticCount(countCommentsItem),
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                }
            )

            val likesItem = statistics.getItemByType(StatisticType.LIKES)
            val countLikesItem = likesItem.count ?: 0
            IconWithText(
                iconResId = if (isLiked) R.drawable.like_set else R.drawable.like,
                text = formatStatisticCount(countLikesItem),
                tint = if (isLiked) DarkRed else Color.Gray,
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                }
            )
        }
    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    } else if (count > 1000) {
        String.format("%.1fK", (count / 1000f))
    } else {
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("Неверный тип для StatisticsBar")
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: (() -> Unit)? = null,
    tint: Color = Color.Gray
) {
    val modifier = if (onItemClickListener == null) {
        Modifier
    } else {
        Modifier.clickable {
            onItemClickListener()
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            painter = painterResource(iconResId), contentDescription = "",
            modifier = Modifier
                .size(20.dp),
            tint = tint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = tint
        )
    }
}

@Composable
private fun SetMoreButton() {
    Icon(
        imageVector = Icons.Rounded.MoreVert,
        contentDescription = "",
        modifier = Modifier
            .size(25.dp),
        tint = MaterialTheme.colorScheme.onSurface
    )
}

