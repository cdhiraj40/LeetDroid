package com.cdhiraj40.leetdroid.model

import java.io.Serializable

class RandomQuestionModel : Serializable {
    /**
     * data
     * randomQuestion": { "titleSlug": "zuma-game" } }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        var randomQuestion: RandomQuestionNode? = null

        class RandomQuestionNode : Serializable {
            var titleSlug: String? = null
        }
    }
}
