package com.cdhiraj40.leetdroid.model

import java.io.Serializable

class QuestionSolutionModel : Serializable {
    /**
     * data: { "question": { "questionId": "1", "article": "{\"id\": 7, \"url\": \"/articles/two-sum/\", \"topicId\": 127810}", "solution": { "id": "7", "content":"content", "contentTypeId": "107",
     * "canSeeDetail": true, "paidOnly": false, "hasVideoSolution": true, "paidOnlyVideo": true, "rating": { "id": "4", "count": 2794, "average": "4.730" } } } } }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        var question: QuestionNode? = null

        class QuestionNode : Serializable {
            var questionId: String? = null
            var article: String? = null
            var solution: SolutionNode? = null

            class SolutionNode : Serializable {
                var id: String? = null
                var content: String? = null
                var contentTypeId: String? = null
                var canSeeDetail: Boolean? = null
                var paidOnly: Boolean? = null
                var hasVideoSolution: Boolean? = null
                var paidOnlyVideo: Boolean? = null
                var rating: RatingNode? = null

                class RatingNode : Serializable {
                    var id: String? = null
                    var count: Int? = null
                    var average: String? = null
                }
            }
        }
    }
}