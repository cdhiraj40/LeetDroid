package com.example.leetdroid.model

import java.io.Serializable

class GeneralDiscussionModel : Serializable {

    /**
     * data :
     * "categoryTopicList": {
     * "totalNum": 10087,
     * "edges": [ {
     * "node": {   "id": "126698",    "title": "How to write an Interview Question post",        "commentCount": 86,             "viewCount": 57854,         "tags": [],
     * "post": {   "id": 254318,      "voteCount": 149,     "creationDate": 1524865315,
     * "author": {     "username": "LeetCode",
     * "profile": {
     * "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png" } } } } } ] } }
     * }
     */
    var data: DataNode? = null

    class DataNode : Serializable {

        /**
         * categoryTopicList :
         * "totalNum": 10087,
         * "edges": [ {
         * "node": {   "id": "126698",    "title": "How to write an Interview Question post",        "commentCount": 86,             "viewCount": 57854,         "tags": [],
         * "post": {   "id": 254318,      "voteCount": 149,     "creationDate": 1524865315,
         * "author": {     "username": "LeetCode",
         * "profile": {
         * "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png" } } } } } ] } }
         * }
         */
        var categoryTopicList: CategoryTopicListNode? = null

        class CategoryTopicListNode : Serializable {

            /**
             * "totalNum": 10087,
             * "edges": [ {
             * "node": {   "id": "126698",    "title": "How to write an Interview Question post",        "commentCount": 86,             "viewCount": 57854,         "tags": [],
             * "post": {   "id": 254318,      "voteCount": 149,     "creationDate": 1524865315,
             * "author": {     "username": "LeetCode",
             * "profile": {
             * "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png" } } } } } ] } }
             */
            var totalNum = 0
            var edges: List<EdgesNode>? = null

            class EdgesNode : Serializable {

                /**
                 *  edges: [ {
                 * "node": {   "id": "126698",    "title": "How to write an Interview Question post",        "commentCount": 86,             "viewCount": 57854,         "tags": [],
                 * "post": {   "id": 254318,      "voteCount": 149,     "creationDate": 1524865315,
                 * "author": {     "username": "LeetCode",
                 * "profile": {
                 * "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png" } } } } } ] } }
                 */
                var node: Node? = null

                class Node : Serializable {

                    var id = 0
                    var title: String? = null
                    var viewCount = 0
                    var commentCount = 0
                    var post: PostNode? = null

                    class PostNode : Serializable {
                        var id = 0
                        var voteCount = 0
                        var creationDate: Long = 0
                        var author: AuthorNode? = null

                        class AuthorNode : Serializable {
                            /**
                             * username : LeetCode
                             * profile : {"userSlug":"nathan_fegard","userAvatar":"https://assets.leetcode.com/users/leetcode/avatar_1568224780.png"}
                             */
                            var username: String? = null
                            var profile: ProfileNode? = null

                            class ProfileNode : Serializable {
                                /**
                                 * userSlug : LeetCode
                                 * userAvatar : https://assets.leetcode.com/users/leetcode/avatar_1568224780.png
                                 */
                                var userSlug: String? = null
                                var userAvatar: String? = null
                            }
                        }
                    }
                }
            }
        }
    }
}