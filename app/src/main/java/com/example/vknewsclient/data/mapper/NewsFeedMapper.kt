package com.example.vknewsclient.data.mapper

import android.util.Log
import androidx.core.net.toUri
import com.example.vknewsclient.data.model.RootResponseCommentsDto
import com.example.vknewsclient.data.model.RootResponseNewsFeedDto
import com.example.vknewsclient.data.model.RootResponseStories
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.domain.StatisticItem
import com.example.vknewsclient.domain.StatisticType
import com.example.vknewsclient.domain.Story
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    private val TAG = "NewsFeedMapper"
    fun mapResponseToPost(responsDto: RootResponseNewsFeedDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()
        val posts = responsDto.newsFeedContent.posts.filter {
            it != null
        }
        val groups = responsDto.newsFeedContent.groups

        for (post in posts) {
            val isLiked = if (post.likes?.userLikes == null || post.likes.userLikes == 0) {
                false
            } else {
                true
            }
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = mapTimesstampToDate(post.date),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url, //последний элемент самого хорошего качества
                statistics = listOf(
                    StatisticItem(
                        type = StatisticType.LIKES,
                        count = post.likes?.userLikes ?: 0
                    ),
                    StatisticItem(
                        type = StatisticType.SHARES,
                        count = post.reposts?.count ?: 0
                    ),
                    StatisticItem(
                        type = StatisticType.COMMENTS,
                        count = post.comments?.count ?: 0
                    ),
                    StatisticItem(
                        type = StatisticType.VIEWS,
                        count = 999
                    )
                ),
                isLiked = isLiked
            )
            Log.d(TAG, "mapResponseToPost: feedPost = $feedPost")
            if (feedPost == null) {
                break
            }
            result.add(feedPost)
        }
        return result
    }

    fun mapResponseToComments(response: RootResponseCommentsDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val commentList = response.content.posts
        val profiles = response.content.users

        for (comment in commentList) {
            //Ищем автора коммента, если такой id не найден, то при помощи continue мы перейдем к след элементу
            val author = profiles.firstOrNull {

                Log.d(
                    TAG,
                    "mapResponseToComments: it.id == comment.authorId = ${it.id == comment.authorId}"
                )
                it.id == comment.authorId
            } ?: continue

            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} + ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                commentText = comment.text,
                publicationDate = mapTimesstampToDate(comment.date)
            )

            result.add(postComment)
        }
        return result
    }

    fun mapStoryDtoToStory(response: RootResponseStories): List<Story> {
        val tempStoriesList = mutableListOf<Story>()
        val objectStories = response.storiesResponse.objectStories

        for (obj in objectStories) {
            val storiesList = obj.stories

            for (stories in storiesList) {

                val story = Story(
                    id = stories.id,
                    ownerId = stories.owner_id,
                    photoUrl = stories.photo?.photoUrls?.lastOrNull()?.url ?: "",
                    type = obj.type,
                    typeInside = stories.type,
                    contentUri = stories.video?.files?.hls?.toUri(),
                    date = mapTimesstampToDate(stories.date),
                    expires_at = mapTimesstampToDate(stories.expires_at),
                    has_unseen = obj.has_unseen,
                    imageStoryUrl = stories.photo?.photoUrls?.lastOrNull()?.url ?: ""
                )

                tempStoriesList.add(story)
            }
        }

        //Метод groupBy группирует в Map
//        val result = tempStoriesList
//            .filter {
//                it.imageStoryUrl.isNotBlank()
//            }
//            .groupBy {
//                it.ownerId
//            }

        val result = tempStoriesList
            .filter {
                it.imageStoryUrl.isNotBlank()
            }


        Log.d(TAG, "mapStoryDtoToStory: Выводим")
        for (lst in result) {
            Log.d(TAG, "mapStoryDtoToStory: temp: $lst")
        }

        return result
    }

    private fun mapTimesstampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000) //Форматируем с помощью объекта Date из пакета java.util
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}