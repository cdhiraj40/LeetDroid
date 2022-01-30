package com.example.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the all questions list
 */
class DailyQuestionModel : Serializable {


    /**
     * data : { "activeDailyCodingChallengeQuestion": { "date": "2022-01-30", "userStatus": "NotStart", "link": "/problems/rotate-array/",
     * "question": { "acRate": 38.02651800583092, "difficulty": "Medium", "freqBar": null, "frontendQuestionId": "189", "isFavor": false,
     * "paidOnly": false, "status": null, "title": "Rotate Array", "titleSlug": "rotate-array", "hasVideoSolution": false, "hasSolution": true,
     * "topicTags": [ { "name": "Array", "id": "VG9waWNUYWdOb2RlOjU=", "slug": "array" }, { "name": "Math", "id": "VG9waWNUYWdOb2RlOjg=", "slug": "math" },
     * { "name": "Two Pointers", "id": "VG9waWNUYWdOb2RlOjk=", "slug": "two-pointers" } ] } } } }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        /**
         * "activeDailyCodingChallengeQuestion": { "date": "2022-01-30", "link": "/problems/rotate-array/","question": { "acRate": 38.02651800583092, difficulty": "Medium",
         *  "frontendQuestionId": "189","paidOnly": false, "title": "Rotate Array","titleSlug": "rotate-array", "hasVideoSolution": false, "hasSolution": true,
         * "topicTags": [ { "name": "Array", "id": "VG9waWNUYWdOb2RlOjU=", "slug": "array" }, { "name": "Math", "id": "VG9waWNUYWdOb2RlOjg=", "slug": "math" },{ "name": "Two Pointers",
         * "id": "VG9waWNUYWdOb2RlOjk=", "slug": "two-pointers" } ] } } } }
         */

        var activeDailyCodingChallengeQuestion: ActiveDailyCodingChallengeQuestionNode? = null

        class ActiveDailyCodingChallengeQuestionNode : Serializable {

            var date: String? = null
            var link: String? = null
            var question: QuestionNode? = null

            class QuestionNode : Serializable {

                var title: String? = null
                var titleSlug: String? = null
                var difficulty: String? = null
                var acRate: Float? = null
                var paidOnly: Boolean? = null
                var frontendQuestionId: String? = null
                var hasVideoSolution: Boolean? = null
                var hasSolution: Boolean? = null
                var topicTags: List<TopicTagsNode>? = null

                class TopicTagsNode : Serializable {

                    /**
                     * topicTags: [ { "name": "Array", "id": "VG9waWNUYWdOb2RlOjU=","slug": "array"},{"name": "Hash Table","id": "VG9waWNUYWdOb2RlOjY=","slug": "hash-table" } ] } ] } } }
                     */

                    var name: String? = null
                    var id: String? = null
                    var slug: String? = null
                }

            }
        }
    }
}