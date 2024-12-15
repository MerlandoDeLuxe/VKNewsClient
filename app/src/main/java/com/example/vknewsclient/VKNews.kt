package com.example.vknewsclient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.example.vknewsclient.ui.theme.VKNewsClientTheme

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onViewsClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
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
            modifier = Modifier.padding(8.dp)
        ) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feedPost.contentText,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            SetImageNews(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            SetStatisticsBar(
                statistics = feedPost.statistics,
                onViewsClickListener = onViewsClickListener,
                onShareClickListener = onShareClickListener,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
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
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        SetMoreButton()
    }
}

@Composable
private fun SetGroupAvatar(
    feedPost: FeedPost
) {
    Image(
        modifier = Modifier
//            .padding(10.dp)
            .clip(CircleShape)
            .background(Color.White)
            .size(80.dp),
        painter = painterResource(feedPost.avatarResId),
        contentDescription = "",
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun SetImageNews(
    feedPost: FeedPost
) {
    Image(
        painter = painterResource(feedPost.contentImageResId),
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(0.5.dp, Color.Gray),
        contentScale = ContentScale.FillWidth   //Растягивает картинку сохраняя соотношение сторон
    )
}

@Composable
private fun SetStatisticsBar(
    statistics: List<StatisticItem>,
    onViewsClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLikeClickListener: (StatisticItem) -> Unit
) {
    Row {
        Row(modifier = Modifier.weight(1f)) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = viewsItem.type.iconResId,
                viewsItem.count.toString(),
                onItemClickListener = {
                    onViewsClickListener(viewsItem)
                }
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            IconWithText(
                iconResId = sharesItem.type.iconResId,
                text = sharesItem.count.toString(),
                onItemClickListener = {
                    onShareClickListener(sharesItem)
                }
            )

            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                iconResId = commentsItem.type.iconResId,
                commentsItem.count.toString(),
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                }
            )

            val likesItem = statistics.getItemByType(StatisticType.LIKES)
            IconWithText(
                iconResId = likesItem.type.iconResId,
                likesItem.count.toString(),
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                }
            )
        }
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
    onItemClickListener: () -> Unit
) {
    Row(
        modifier = Modifier.clickable {
            onItemClickListener()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconResId), contentDescription = "",
            modifier = Modifier
                .size(18.dp),
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary
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

private val feedPost = FeedPost()

@Preview
@Composable
private fun PreviewLightTheme(
) {
    VKNewsClientTheme(
        darkTheme = false
    ) {
        NewsCard(
            feedPost = feedPost,
            onViewsClickListener = {},
            onShareClickListener = {},
            onCommentClickListener = {},
            onLikeClickListener = {},
        )
    }
}

@Preview
@Composable
private fun PreviewDarkTheme() {
    VKNewsClientTheme(
        darkTheme = true
    ) {
        NewsCard(
            feedPost = feedPost,
            onViewsClickListener = {},
            onShareClickListener = {},
            onCommentClickListener = {},
            onLikeClickListener = {},
        )
    }
}

