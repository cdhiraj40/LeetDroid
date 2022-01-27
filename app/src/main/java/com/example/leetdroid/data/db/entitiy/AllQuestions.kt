package com.example.leetdroid.data.db.entitiy

import androidx.room.Entity
import com.example.leetdroid.model.AllQuestionsModel
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "all_questions")
//data class AllQuestions(
//    @SerializedName("all_questions")
//    val data: DataNode
//
//    class DataNode : Serializable {
//
//        var problemsetQuestionList: ProblemSetQuestionListNode? = null
//
//            var total: Int = 0
//            var questions: List<Questions>? = null
//
//            class Questions : Serializable {
//
//                var title: String? = null
//                var titleSlug: String? = null
//                var difficulty: String? = null
//                var acRate: Float? = null
//                var paidOnly: Boolean? = null
//                var frontendQuestionId: String? = null
//                var topicTags: List<TopicTagsNode>? = null
//
//                class TopicTagsNode : Serializable {
//
//                    var name: String? = null
//                    var id: String? = null
//                    var slug: String? = null
//                }
//                var hasSolution: Boolean? = null
//            }
//        }
//    }
//}
//)
