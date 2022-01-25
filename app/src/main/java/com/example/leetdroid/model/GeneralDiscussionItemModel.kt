package com.example.leetdroid.model

import java.io.Serializable


class GeneralDiscussionItemModel : Serializable {
    /**
    data : { "topic": { "id": 126698, "viewCount": 57949, "topLevelCommentCount": 68, "title": "How to write an Interview Question post", "pinned": true, "tags": [], "post": { "id": 254318, "voteCount": 149,
    "content": "**LeetCode Discuss**\\xA0is a community where you can share questions and experiences from interviews.\\nBefore you post, please try to follow these guidelines:\\n\\n- The title should follow
    this format: \\n\\t```\\n\\tCompany Name | Stage (Phone, Onsite, OA) | Question name\\n\\t```\\n\\t\\n\\tFor example, this could look like: \\n\\t```\\n\\tAmazon | Phone | Missing number\\n\\tGoogle |
    Onsite | Robot Room Cleaner\\n\\t```\\n\\n- Please post\\xA0**clear**\\xA0and\\xA0**concise**\\xA0descriptions of the problems and make sure your question has not already been posted.\\n\\n- If you\\'re
    including **code in your post**, please **surround the code block with three backticks** (\\\\`\\\\`\\\\`) to make it more readable. Posts that do not follow this guideline may be removed by moderators.
    \\n\\n-  Each question should be accompanied by\\xA0**at least**\\xA0one example - the more the better!\\n\\n-  Please enter the company name in the tags shown below which will help people see your question
    more easily.\\n\\n- Each post should include only one question. If you want to share your overall interview experience, please post in the **Interview Experience** category.\\n\\n**Happy discussing!** :)",
    "updationDate": 1602370486,
    "creationDate": 1524865315,
    "author": {
    "username": "LeetCode",
    "profile": {
    "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png",
    "reputation": 15825
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        /**
         * topic": { "id": 126698, "viewCount": 57949, "topLevelCommentCount": 68, "title": "How to write an Interview Question post", "pinned": true, "tags": [], "post": { "id": 254318, "voteCount": 149,
         * "content": "**LeetCode Discuss**\\xA0is a community where you can share questions and experiences from interviews.\\nBefore you post, please try to follow these guidelines:\\n\\n- The title should follow
        this format: \\n\\t```\\n\\tCompany Name | Stage (Phone, Onsite, OA) | Question name\\n\\t```\\n\\t\\n\\tFor example, this could look like: \\n\\t```\\n\\tAmazon | Phone | Missing number\\n\\tGoogle |
        Onsite | Robot Room Cleaner\\n\\t```\\n\\n- Please post\\xA0**clear**\\xA0and\\xA0**concise**\\xA0descriptions of the problems and make sure your question has not already been posted.\\n\\n- If you\\'re
        including **code in your post**, please **surround the code block with three backticks** (\\\\`\\\\`\\\\`) to make it more readable. Posts that do not follow this guideline may be removed by moderators.
        \\n\\n-  Each question should be accompanied by\\xA0**at least**\\xA0one example - the more the better!\\n\\n-  Please enter the company name in the tags shown below which will help people see your question
        more easily.\\n\\n- Each post should include only one question. If you want to share your overall interview experience, please post in the **Interview Experience** category.\\n\\n**Happy discussing!** :)",
         *  "updationDate": 1602370486,
         *  "creationDate": 1524865315,
         *  "author": {
         *  "username": "LeetCode",
         *  "profile": {
         *  "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png",
         *  "reputation": 15825
         */
        var topic: TopicNode? = null

        class TopicNode : Serializable {
            /**
             * "id": 126698, "viewCount": 57949, "topLevelCommentCount": 68, "title": "How to write an Interview Question post", "pinned": true, "tags": [], "post": { "id": 254318, "voteCount": 149,
             * "content": "**LeetCode Discuss**\\xA0is a community where you can share questions and experiences from interviews.\\nBefore you post, please try to follow these guidelines:\\n\\n- The title should follow
            this format: \\n\\t```\\n\\tCompany Name | Stage (Phone, Onsite, OA) | Question name\\n\\t```\\n\\t\\n\\tFor example, this could look like: \\n\\t```\\n\\tAmazon | Phone | Missing number\\n\\tGoogle |
            Onsite | Robot Room Cleaner\\n\\t```\\n\\n- Please post\\xA0**clear**\\xA0and\\xA0**concise**\\xA0descriptions of the problems and make sure your question has not already been posted.\\n\\n- If you\\'re
            including **code in your post**, please **surround the code block with three backticks** (\\\\`\\\\`\\\\`) to make it more readable. Posts that do not follow this guideline may be removed by moderators.
            \\n\\n-  Each question should be accompanied by\\xA0**at least**\\xA0one example - the more the better!\\n\\n-  Please enter the company name in the tags shown below which will help people see your question
            more easily.\\n\\n- Each post should include only one question. If you want to share your overall interview experience, please post in the **Interview Experience** category.\\n\\n**Happy discussing!** :)",
             *  "updationDate": 1602370486,
             *  "creationDate": 1524865315,
             *  "author": {
             *  "username": "LeetCode",
             *  "profile": {
             *  "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png",
             *  "reputation": 15825
             */
            var id = 0
            var viewCount = 0
            var topLevelCommentCount = 0
            var title: String? = null
            var pinned: Boolean? = null
            var post: PostNode? = null

            class PostNode : Serializable {
                /**
                 * id: 254318
                 * voteCount: 149
                 * "content": "**LeetCode Discuss**\\xA0is a community where you can share questions and experiences from interviews.\\nBefore you post, please try to follow these guidelines:\\n\\n- The title should follow
                this format: \\n\\t```\\n\\tCompany Name | Stage (Phone, Onsite, OA) | Question name\\n\\t```\\n\\t\\n\\tFor example, this could look like: \\n\\t```\\n\\tAmazon | Phone | Missing number\\n\\tGoogle |
                Onsite | Robot Room Cleaner\\n\\t```\\n\\n- Please post\\xA0**clear**\\xA0and\\xA0**concise**\\xA0descriptions of the problems and make sure your question has not already been posted.\\n\\n- If you\\'re
                including **code in your post**, please **surround the code block with three backticks** (\\\\`\\\\`\\\\`) to make it more readable. Posts that do not follow this guideline may be removed by moderators.
                \\n\\n-  Each question should be accompanied by\\xA0**at least**\\xA0one example - the more the better!\\n\\n-  Please enter the company name in the tags shown below which will help people see your question
                more easily.\\n\\n- Each post should include only one question. If you want to share your overall interview experience, please post in the **Interview Experience** category.\\n\\n**Happy discussing!** :)",
                 *  "updationDate": 1602370486,
                 *  "creationDate": 1524865315,
                 *  "author": {
                 *  "username": "LeetCode",
                 *  "profile": {
                 *  "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png",
                 *  "reputation": 15825
                 */
                var id = 0
                var voteCount = 0
                var content: String? = null
                var creationDate = 0
                var updationDate = 0
                var author: AuthorNode? = null

                class AuthorNode : Serializable {
                    /**
                     *  "username": "LeetCode",
                     *  "profile": {
                     *  "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png",
                     *  "reputation": 15825
                     */
                    var username: String? = null
                    var profile: ProfileNode? = null

                    class ProfileNode : Serializable {
                        /**
                         *  "userAvatar": "https://assets.leetcode.com/users/leetcode/avatar_1568224780.png",
                         *  "reputation": 15825
                         */
                        var userAvatar: String? = null
                        var reputation = 0
                    }
                }
            }
        }
    }
}