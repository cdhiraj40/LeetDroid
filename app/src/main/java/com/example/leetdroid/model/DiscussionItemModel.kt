package com.example.leetdroid.model

import java.io.Serializable


class DiscussionItemModel:Serializable {
    /**
     * { data: { "topic": { "id": 17, "viewCount": 292895, "title": "Here is a Python solution in O(n) time", "post": { "id": 17, "voteCount": 837, "content": "The key to the problem is that there is ALWAYS only 1 pair of numbers that satisfy the
     * condition of adding together to be the target value. \\nWe can assume that for all the numbers in the list (**x1, x2, ... xn**) that there exists a pair such that **xa + xb = target** \\nTo solve this with a single pass of the list we can
     * change the equation above to **xa = target - xb** and since we know the target as long as we maintain a record of all previous values in the list we can compare the current value (**xa**) to it\\'s ONLY pair, if it exists, in record of all
     * previous values (**xb**)\\n\\nTo keep a record of the previous values and their indices I have used a dictionary. Commonly known as a map in other languages. This allows me to record each previous number in the dictionary alongside the
     * indice as a key value pair (target-number, indice). \\n```\\nclass Solution(object):\\n\\tdef twoSum(self, nums, target):\\n\\t\\tbuffer_dictionary = {}\\n\\t\\tfor i in rangenums.__len()):\\n\\t\\t\\tif nums[i] in buffer_dictionary:\\n\\t
     * \\t\\t\\treturn [buffer_dictionary[nums[i]], i] #if a number shows up in the dictionary already that means the \\n\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t#necesarry pair has been iterated on previously\\n\\t\\t\\telse: # else is entirely
     * optional\\n\\t\\t\\t\\tbuffer_dictionary[target - nums[i]] = i \\n\\t\\t\\t\\t# we insert the required number to pair with should it exist later in the list of numbers\\n```",
     * "updationDate": 1605556031, "creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "profile": { "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png", "reputation": 953 } } } } } }
     */
    var data: DataNode? = null

    class DataNode:Serializable {
        /**
         * topic : { "id": 17, "viewCount": 292895, "title": "Here is a Python solution in O(n) time", "post": { "id": 17, "voteCount": 837, "content": "The key to the problem is that there is ALWAYS only 1 pair of numbers that satisfy the
         * condition of adding together to be the target value. \\nWe can assume that for all the numbers in the list (**x1, x2, ... xn**) that there exists a pair such that **xa + xb = target** \\nTo solve this with a single pass of the list we can
         * change the equation above to **xa = target - xb** and since we know the target as long as we maintain a record of all previous values in the list we can compare the current value (**xa**) to it\\'s ONLY pair, if it exists, in record of all
         * previous values (**xb**)\\n\\nTo keep a record of the previous values and their indices I have used a dictionary. Commonly known as a map in other languages. This allows me to record each previous number in the dictionary alongside the
         * indice as a key value pair (target-number, indice). \\n```\\nclass Solution(object):\\n\\tdef twoSum(self, nums, target):\\n\\t\\tbuffer_dictionary = {}\\n\\t\\tfor i in rangenums.__len()):\\n\\t\\t\\tif nums[i] in buffer_dictionary:\\n\\t
         * \\t\\t\\treturn [buffer_dictionary[nums[i]], i] #if a number shows up in the dictionary already that means the \\n\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t#necesarry pair has been iterated on previously\\n\\t\\t\\telse: # else is entirely
         * optional\\n\\t\\t\\t\\tbuffer_dictionary[target - nums[i]] = i \\n\\t\\t\\t\\t# we insert the required number to pair with should it exist later in the list of numbers\\n```",
         * "updationDate": 1605556031, "creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "profile": { "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png", "reputation": 953 } } } } } }
         */
        var topic: TopicNode? = null

        class TopicNode:Serializable {
            /**
             * post : { "id": 17, "voteCount": 837, "content": "The key to the problem is that there is ALWAYS only 1 pair of numbers that satisfy the condition of adding together to be the target value. \\nWe can assume that for all the numbers in the list (**x1, x2, ... xn**) that there exists a pair such that **xa + xb = target** \\nTo solve this with a single pass of the list we can
             * change the equation above to **xa = target - xb** and since we know the target as long as we maintain a record of all previous values in the list we can compare the current value (**xa**) to it\\'s ONLY pair, if it exists, in record of all
             * previous values (**xb**)\\n\\nTo keep a record of the previous values and their indices I have used a dictionary. Commonly known as a map in other languages. This allows me to record each previous number in the dictionary alongside the
             * indice as a key value pair (target-number, indice). \\n```\\nclass Solution(object):\\n\\tdef twoSum(self, nums, target):\\n\\t\\tbuffer_dictionary = {}\\n\\t\\tfor i in rangenums.__len()):\\n\\t\\t\\tif nums[i] in buffer_dictionary:\\n\\t
             * \\t\\t\\treturn [buffer_dictionary[nums[i]], i] #if a number shows up in the dictionary already that means the \\n\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t#necesarry pair has been iterated on previously\\n\\t\\t\\telse: # else is entirely
             * optional\\n\\t\\t\\t\\tbuffer_dictionary[target - nums[i]] = i \\n\\t\\t\\t\\t# we insert the required number to pair with should it exist later in the list of numbers\\n```",
             * "updationDate": 1605556031, "creationDate": 1440998099, "author": { "username": "Nathan_Fegard", "profile": { "userAvatar": "https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png", "reputation": 953 } } } } } }
             */
            var id = 0
            var viewCount = 0
            var title: String? = null
            var post: PostNode? = null

            class PostNode:Serializable {
                /**
                 * id : 17
                 * voteCount : 481
                 * content :    "The key to the problem is that there is ALWAYS only 1 pair of numbers that satisfy the condition of adding together to be the target value. \\nWe can assume that for all the numbers in the list (**x1, x2, ... xn**) that there
                 * exists a pair such that **xa + xb = target** \\nTo solve this with a single pass of the list we can change the equation above to **xa = target - xb** and since we know the target as long as we maintain a record of all previous values in
                 * the list we can compare the current value (**xa**) to it\\'s ONLY pair, if it exists, in record of all previous values (**xb**)\\n\\nTo keep a record of the previous values and their indices I have used a dictionary. Commonly known as
                 * a map in other languages. This allows me to record each previous number in the dictionary alongside the indice as a key value pair (target-number, indice). \\n```\\nclass Solution(object):\\n\\tdef twoSum(self, nums, target):\\n\\t\\t
                 * buffer_dictionary = {}\\n\\t\\tfor i in rangenums.__len()):\\n\\t\\t\\tif nums[i] in buffer_dictionary:\\n\\t \\t\\t\\treturn [buffer_dictionary[nums[i]], i] #if a number shows up in the dictionary already that means the
                 * \\n\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t#necesarry pair has been iterated on previously\\n\\t\\t\\telse: # else is entirely optional\\n\\t\\t\\t\\tbuffer_dictionary[target - nums[i]] = i \\n\\t\\t\\t\\t# we insert the required
                 * number to pair with should it exist later in the list of numbers\\n```"
                 * updationDate : 1605556031
                 * creationDate : 1440998099
                 * author : {"username":"Nathan_Fegard","profile":{"userAvatar":"https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png","reputation":953}}
                 */
                var id = 0
                var voteCount = 0
                var content: String? = null
                var updationDate = 0
                var author: AuthorNode? = null

                class AuthorNode:Serializable {
                    /**
                     * username : Nathan_Fegard
                     * profile : {"userAvatar":"https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png","reputation":953}
                     */
                    var username: String? = null
                    var profile: ProfileNode? = null

                    class ProfileNode:Serializable {
                        /**
                         * userAvatar : https://assets.leetcode.com/users/nathan_fegard/avatar_1536531058.png
                         * reputation : 953
                         */
                        var userAvatar: String? = null
                        var reputation = 0
                    }
                }
            }
        }
    }
}