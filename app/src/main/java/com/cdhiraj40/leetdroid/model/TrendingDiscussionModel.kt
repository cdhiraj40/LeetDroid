package com.cdhiraj40.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the all questions list' error
 *  if no data comes we check for error here
 */
class TrendingDiscussionModel : Serializable {

    var data: DataNode? = null

    class DataNode : Serializable {
        var cachedTrendingCategoryTopics: List<CachedTrendingCategoryTopics>? = null

        class CachedTrendingCategoryTopics : Serializable {
            val id: Int? = null
            val post: PostNode? = null
            val title: String? = null

            class PostNode : Serializable {
                val author: AuthorNode? = null
                val contentPreview: String? = null
                val creationDate: Int? = null
                val id: Int? = null

                class AuthorNode : Serializable {
                    val isActive: Boolean? = null
                    val profile: ProfileNode? = null
                    val username: String? = null

                    class ProfileNode : Serializable {
                        val userAvatar: String? = null
                    }
                }
            }
        }
    }
}