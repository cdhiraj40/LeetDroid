package com.cdhiraj40.leetdroid.model

import java.io.Serializable

class ContestRankingModel : Serializable {
    /**
     * data :  "userContestRanking": { "attendedContestsCount": 1, "rating": 1465.221, "globalRanking": 131082 },
     * "userContestRankingHistory": [ { "contest": { "title": "Weekly Contest 2", "startTime": 1472347800 }, "rating": 1500.0, "ranking": 0 } ] }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        var userContestRanking: UserContestRankingNode? = null

        class UserContestRankingNode : Serializable {
            var attendedContestsCount: Int? = null
            var rating: Double? = null
            var globalRanking: Long? = null
        }

        var userContestRankingHistory: List<UserContestRankingHistoryNode>? = null

        class UserContestRankingHistoryNode : Serializable {
            var contest: ContestNode? = null
            var rating: Double? = null
            var ranking: Long? = null

            class ContestNode {
                var title: String? = null
                var startTime: Long? = null
            }
        }
    }
}