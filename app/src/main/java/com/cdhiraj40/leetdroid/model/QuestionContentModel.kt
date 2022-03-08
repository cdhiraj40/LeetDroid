package com.cdhiraj40.leetdroid.model

import java.io.Serializable


class QuestionContentModel : Serializable {

    /**
     * data : {
     * "question": {
     * "questionId": "1",
     * "questionFrontendId": "1",
     * "title": "Two Sum",
     * "titleSlug": "two-sum",
     * "content": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.",
     * "difficulty": "Easy",
     * "likes": 28131,
     * "dislikes": 902,
     * "exampleTestcases": "[2,7,11,15]\n9\n[3,2,4]\n6\n[3,3]\n6",
     * "categoryTitle": "Algorithms",
     * "topicTags": [ {     "name": "Array","slug": "array",},{"name": "Hash Table","slug": "hash-table",}     ],
     * "stats": "{ totalAccepted : \"5.7M\", \"totalSubmission\": \"11.9M\", \"totalAcceptedRaw\": 5718886, \"totalSubmissionRaw\": 11887372, \"acRate\": \"48.1%\"}",
     * "hints": ["A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions
     * for just for completeness. It is from these brute force solutions that you can come up with optimizations.","So, if we fix one of the numbers, say <pre>x</pre>, we have to scan the entire array to find the next number <pre>y</pre> which is <pre>value - x</pre> where value is the input parameter. Can we change our array somehow so that this search becomes faster?",
     * "The second train of thought is, without changing the array, can we use additional space somehow? Like maybe a hash map to speed up the search?"] } } }
     */

    var data: DataNode? = null

    class DataNode : Serializable {

        /**
         * question : {
         * "questionId": "1",
         * "questionFrontendId": "1",
         * "title": "Two Sum",
         * "titleSlug": "two-sum",
         * "content": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
         * You may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.",
         * "difficulty": "Easy",
         * "likes": 28131,
         * "dislikes": 902,
         * "exampleTestcases": "[2,7,11,15]\n9\n[3,2,4]\n6\n[3,3]\n6",
         * "categoryTitle": "Algorithms",
         * "topicTags": [ {     "name": "Array","slug": "array",},{"name": "Hash Table","slug": "hash-table",}     ],
         * "stats": "{ totalAccepted : \"5.7M\", \"totalSubmission\": \"11.9M\", \"totalAcceptedRaw\": 5718886, \"totalSubmissionRaw\": 11887372, \"acRate\": \"48.1%\"}",
         * "hints": ["A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions
         * for just for completeness. It is from these brute force solutions that you can come up with optimizations.","So, if we fix one of the numbers, say <pre>x</pre>, we have to scan the entire array to find the next number <pre>y</pre> which is <pre>value - x</pre> where value is the input parameter. Can we change our array somehow so that this search becomes faster?",
         * "The second train of thought is, without changing the array, can we use additional space somehow? Like maybe a hash map to speed up the search?"] } } }
         */

        var question: QuestionNode? = null

        class QuestionNode : Serializable {

            /**
             * "questionId": "1",
             * "questionFrontendId": "1",
             * "title": "Two Sum",
             * "titleSlug": "two-sum",
             * "content": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
             * You may assume that each input would have exactly one solution, and you may not use the same element twice. You can return the answer in any order.",
             * "difficulty": "Easy",
             * "likes": 28131,
             * "dislikes": 902,
             * "exampleTestcases": "[2,7,11,15]\n9\n[3,2,4]\n6\n[3,3]\n6",
             * "categoryTitle": "Algorithms",
             * "topicTags": [ {     "name": "Array","slug": "array",},{"name": "Hash Table","slug": "hash-table",}     ],
             * "stats": "{ totalAccepted : \"5.7M\", \"totalSubmission\": \"11.9M\", \"totalAcceptedRaw\": 5718886, \"totalSubmissionRaw\": 11887372, \"acRate\": \"48.1%\"}",
             * "hints": ["A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions
             * for just for completeness. It is from these brute force solutions that you can come up with optimizations.","So, if we fix one of the numbers, say <pre>x</pre>, we have to scan the entire array to find the next number <pre>y</pre> which is <pre>value - x</pre> where value is the input parameter. Can we change our array somehow so that this search becomes faster?",
             * "The second train of thought is, without changing the array, can we use additional space somehow? Like maybe a hash map to speed up the search?"] } } }
             */

            var questionId: String? = null
            var questionFrontendId: String? = null
            var title: String? = null
            var titleSlug: String? = null
            var content: String? = null
            var difficulty: String? = null
            var isPaidOnly: Boolean? = null
            var likes: Int? = null
            var dislikes: Int? = null
            var exampleTestcases: String? = null
            var categoryTitle: String? = null

            var topicTags: List<TopicTagsNode>? = null
            var stats: String? = null
            var hints: List<String>? = null

            class TopicTagsNode : Serializable {

                /**
                 * "topicTags": [ {     "name": "Array","slug": "array",},{"name": "Hash Table","slug": "hash-table",}     ],
                 * "stats": "{ totalAccepted : \"5.7M\", \"totalSubmission\": \"11.9M\", \"totalAcceptedRaw\": 5718886, \"totalSubmissionRaw\": 11887372, \"acRate\": \"48.1%\"}",
                 * "hints": ["A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions
                 * for just for completeness. It is from these brute force solutions that you can come up with optimizations.","So, if we fix one of the numbers, say <pre>x</pre>, we have to scan the entire array to find the next number <pre>y</pre> which is <pre>value - x</pre> where value is the input parameter. Can we change our array somehow so that this search becomes faster?",
                 * "The second train of thought is, without changing the array, can we use additional space somehow? Like maybe a hash map to speed up the search?"] } } }
                 */

                var name: String? = null
                var slug: String? = null


                /**
                 * "hints": ["A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions
                 * for just for completeness. It is from these brute force solutions that you can come up with optimizations.","So, if we fix one of the numbers, say <pre>x</pre>, we have to scan the entire array to find the next number <pre>y</pre> which is <pre>value - x</pre> where value is the input parameter. Can we change our array somehow so that this search becomes faster?",
                 * "The second train of thought is, without changing the array, can we use additional space somehow? Like maybe a hash map to speed up the search?"] } } }
                 */

                class SolutionNode : Serializable {
                    var id: String? = null
                    var canSeeDetail: Boolean? = null
                    var paidOnly: Boolean? = null
                    var hasVideoSolution: Boolean? = null
                    var paidOnlyVideo: Boolean? = null


                }

            }

            var solution: TopicTagsNode.SolutionNode? = null
        }
    }

}

// stats come as a string, convert it in json and then use
class StatsNode : Serializable {
    /**
     * "stats": "{ totalAccepted : \"5.7M\", \"totalSubmission\": \"11.9M\", \"totalAcceptedRaw\": 5718886, \"totalSubmissionRaw\": 11887372, \"acRate\": \"48.1%\"}",
     * "hints": ["A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions
     * for just for completeness. It is from these brute force solutions that you can come up with optimizations.","So, if we fix one of the numbers, say <pre>x</pre>, we have to scan the entire array to find the next number <pre>y</pre> which is <pre>value - x</pre> where value is the input parameter. Can we change our array somehow so that this search becomes faster?",
     * "The second train of thought is, without changing the array, can we use additional space somehow? Like maybe a hash map to speed up the search?"] } } }
     */

    var totalAccepted: String? = null
    var totalSubmission: String? = null
    var totalAcceptedRaw: String? = null
    var totalSubmissionRaw: String? = null
    var acRate: String? = null
    var hints: List<String>? = null
}