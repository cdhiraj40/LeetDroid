package com.example.leetdroid.model

import java.io.Serializable

class UserProfileErrorModel : Serializable {

    /**
    errors : [ { "message": "That user does not exist.", "locations": [ { "line": 6, "column": 3 } ],}
     */
    var errors: List<ErrorNode>? = null

    class ErrorNode : Serializable {

        val message: String? = null
    }
}
