package com.example.leetdroid.model

import java.io.Serializable

class QuestionDiscussionsModel : Serializable {
    /**
     * data: { "questionTopicsList": { "totalNum": 7776, "edges": [ { "node": { "id": "17", "title": "Here is a Python solution in O(n) time", "commentCount": 334,"viewCount": 292750,"tags": [],
     * "post": { "id": 17, "voteCount": 837,"creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "isActive": true,"nameColor": null, "activeBadge": null, "profile": {
     * "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png" } }, "status": null } } } ] } } }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        /**
         * questionTopicsList : { "totalNum": 7776, "edges": [ { "node": { "id": "17", "title": "Here is a Python solution in O(n) time", "commentCount": 334,"viewCount": 292750,"tags": [],
         * "post": { "id": 17, "voteCount": 837,"creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "isActive": true,"nameColor": null, "activeBadge": null, "profile": {
         * "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png" } }, "status": null } } } ] } } }
         */
        var questionTopicsList: QuestionTopicsListNode? = null

        class QuestionTopicsListNode : Serializable {
            /**
             * totalNum : 7776
             * edges : [ { "node": { "id": "17", "title": "Here is a Python solution in O(n) time", "commentCount": 334,"viewCount": 292750,"tags": [],
             * "post": { "id": 17, "voteCount": 837,"creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "isActive": true,"nameColor": null, "activeBadge": null, "profile": {
             * "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png" } }, "status": null } } } ] } } }
             */
            var totalNum = 0
            var edges: List<EdgesNode>? = null

            class EdgesNode : Serializable {
                /**
                 *  node: { "id": "17", "title": "Here is a Python solution in O(n) time", "commentCount": 334,"viewCount": 292750,"tags": [],
                 * "post": { "id": 17, "voteCount": 837,"creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "isActive": true,"nameColor": null, "activeBadge": null, "profile": {
                 * "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png" } }, "status": null } } } ] } } }
                 */
                var node: NodeNode? = null

                class NodeNode : Serializable {
                    /**
                     * id : 17
                     * title : Here is a Python solution in O(n) time
                     * viewCount : 292750
                     * commentCount : 334
                     * "tags": []
                     * post : {"id":17,"voteCount":837,"creationDate":1440998099,"author":{"username":"Nathan_Fegard","profile":{"userSlug":"nathan_fegard","userAvatar":"https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png"}}}
                     */
                    var id = 0
                    var title: String? = null
                    var viewCount = 0
                    var commentCount = 0
                    var post: PostNode? = null

                    class PostNode : Serializable {
                        /**
                         * id : 17
                         * voteCount : 837
                         * creationDate : 1440998099
                         * author : {"username":"Nathan_Fegard","profile":{"userSlug":"nathan_fegard","userAvatar":"https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png"}}
                         */
                        var id = 0
                        var voteCount = 0
                        var creationDate: Long = 0
                        var author: AuthorNode? = null

                        class AuthorNode : Serializable {
                            /**
                             * username : Nathan_Fegard
                             * profile : {"userSlug":"nathan_fegard","userAvatar":"https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png"}
                             */
                            var username: String? = null
                            var profile: ProfileNode? = null

                            class ProfileNode : Serializable {
                                /**
                                 * userSlug : nathan_fegard
                                 * userAvatar : https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png
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