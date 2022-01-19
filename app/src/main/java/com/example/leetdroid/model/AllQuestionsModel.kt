package com.example.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the all questions list
 */
class AllQuestionsModel : Serializable {


    /**
     * data: { "problemsetQuestionList": {"total": 2137,
     * "questions": [
     * { "acRate": 48.10413384552075, "difficulty": "Easy", "frontendQuestionId": "1", "paidOnly": false, "title": "Two Sum", "titleSlug": "two-sum", "topicTags": [ { "name": "Array", "id": "VG9waWNUYWdOb2RlOjU=",
     * "slug": "array"},{"name": "Hash Table","id": "VG9waWNUYWdOb2RlOjY=","slug": "hash-table" } ] } ] } } }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        /**
         * problemsetQuestionList: {"total": 2137,"questions": [ { "acRate": 48.10413384552075, "difficulty": "Easy", "frontendQuestionId": "1", "paidOnly": false, "title": "Two Sum",
         * "titleSlug": "two-sum", "topicTags": [ { "name": "Array", "id": "VG9waWNUYWdOb2RlOjU=","slug": "array"},{"name": "Hash Table","id": "VG9waWNUYWdOb2RlOjY=","slug": "hash-table" } ]
         * "hasSolution": true  } ] } } }
         */

        var problemsetQuestionList: ProblemSetQuestionListNode? = null

        class ProblemSetQuestionListNode : Serializable {

            var total: Int = 0
            var questions: List<Questions>? = null

            class Questions : Serializable {

                var title: String? = null
                var titleSlug: String? = null
                var difficulty: String? = null
                var acRate: Float? = null
                var paidOnly: Boolean? = null
                var frontendQuestionId: String? = null
                var topicTags: List<TopicTagsNode>? = null

                class TopicTagsNode : Serializable {

                    /**
                     * topicTags: [ { "name": "Array", "id": "VG9waWNUYWdOb2RlOjU=","slug": "array"},{"name": "Hash Table","id": "VG9waWNUYWdOb2RlOjY=","slug": "hash-table" } ] } ] } } }
                     */

                    var name: String? = null
                    var id: String? = null
                    var slug: String? = null
                }
                var hasSolution: Boolean? = null
            }
        }
    }
}