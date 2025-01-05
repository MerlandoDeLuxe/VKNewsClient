package com.example.vknewsclient.domain.entity

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Immutable  //Означает, что классы 100% одинаковые и не изменятся. Необходимо, чтобы рекомпозиция
//не происходила лишний раз, например, при передаче этого класса в графе Navigation. У этого класса не должно быть полей var
@Parcelize
data class FeedPost @Inject constructor (
    val id: Long,
    val communityId : Long,
    val communityName: String,
    val publicationDate: String,
    val communityImageUrl: String,
    val contentText: String? = "",
    val contentImageUrl: String?,
    val statistics: List<StatisticItem>,
    val isLiked: Boolean
) : Parcelable {
    //создаем свой NavigationType для передачи целого объекта, но передача целого объекта не рекомендуется

    companion object {
        val feedType: NavType<FeedPost> = object : NavType<FeedPost>(false){
            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable(key)
            }

            override fun parseValue(value: String): FeedPost {
                return Gson().fromJson(value, FeedPost::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: FeedPost) {
                bundle.putParcelable(key, value)
            }
        }
    }
}