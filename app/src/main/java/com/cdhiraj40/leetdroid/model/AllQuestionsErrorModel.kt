package com.cdhiraj40.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the all questions list' error
 *  if no data comes we check for error here
 */
class AllQuestionsErrorModel : Serializable {


    /**
     * "errors": [ "message": "2", "locations": [ { "line": 1, "column": 115 } ], "path": [ "problemsetQuestionList" ] } ], "data": { "problemsetQuestionList": null } }
     */
    var errors: List<ErrorsNode>? = null

    class ErrorsNode : Serializable {
        /**
         * "message": "2", "locations": [ { "line": 1, "column": 115 } ], "path": [ "problemsetQuestionList" ] } ], "data": { "problemsetQuestionList": null } }
         */

        var message: String? = null
        var locations: List<LocationsNode>? = null

        class LocationsNode : Serializable {
            var line: Int? = null
            var column: Int? = null
            var path: List<String>? = null
        }
    }

    var data: DataNode? = null

    class DataNode : Serializable {
        var problemsetQuestionList: ProblemSetQuestionListNode? = null

        class ProblemSetQuestionListNode : Serializable {

            var total: Int = 0
        }
    }
}