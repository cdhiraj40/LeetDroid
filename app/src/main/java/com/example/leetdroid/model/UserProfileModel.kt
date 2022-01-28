package com.example.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the user's profile
 */
class UserProfileModel : Serializable {

    /**
     * data: { "matchedUser": { "username": "cdhiraj40","socialAccounts": null, "githubUrl": "https://github.com/cdhiraj40","contributions": { "points": 367, "questionCount": 0, "testcaseCount": 0 }, "profile": { "realName": "Dhiraj Chauhan",
     * "websites": [ "https://linktr.ee/cdhiraj40" ],"countryName": "India", "skillTags": [], "company": null, "school": "Thakur College of Engineering", "starRating": 2, "aboutMe": "CSE TCET'24 | ANDROID DEVELOPER | NIRMAAN HYPERLOOP |3⭐ @codechef",
     * "userAvatar": "https://assets.leetcode.com/users/cdhiraj40/avatar_1630407333.png", "reputation": 0, "ranking": 100001 }, "submitStats": { "acSubmissionNum": [ { "difficulty": "All", "count": 95, "submissions": 109 },
     * { "difficulty": "Easy", "count": 51, "submissions": 58 }, { "difficulty": "Medium", "count": 41, "submissions": 47 }, { "difficulty": "Hard", "count": 3, "submissions": 4 } ],
     *  "badges": [], "upcomingBadges": [], "activeBadge": null } } }
     */

    var data: DataNode? = null

    class DataNode : Serializable {

        /**
         *  matchedUser: { "username": "cdhiraj40","socialAccounts": null, "githubUrl": "https://github.com/cdhiraj40","contributions": { "points": 367, "questionCount": 0, "testcaseCount": 0 }, "profile": { "realName": "Dhiraj Chauhan",
         * "websites": [ "https://linktr.ee/cdhiraj40" ],"countryName": "India", "skillTags": [], "company": null, "school": "Thakur College of Engineering", "starRating": 2, "aboutMe": "CSE TCET'24 | ANDROID DEVELOPER | NIRMAAN HYPERLOOP |3⭐ @codechef",
         * "userAvatar": "https://assets.leetcode.com/users/cdhiraj40/avatar_1630407333.png", "reputation": 0, "ranking": 100001 }, "submitStats": { "acSubmissionNum": [ { "difficulty": "All", "count": 95, "submissions": 109 },
         * { "difficulty": "Easy", "count": 51, "submissions": 58 }, { "difficulty": "Medium", "count": 41, "submissions": 47 }, { "difficulty": "Hard", "count": 3, "submissions": 4 } ],
         *  "badges": [], "upcomingBadges": [], "activeBadge": null } } }
         */

        var matchedUser: MatchedUserNode? = null


        class MatchedUserNode : Serializable {

            /**
             * username: "cdhiraj40"
             * socialAccounts: null
             * githubUrl: "https://github.com/cdhiraj40"
             * contributions: { "points": 367, "questionCount": 0, "testcaseCount": 0 }, profile: { "realName": "Dhiraj Chauhan","websites": [ "https://linktr.ee/cdhiraj40" ],"countryName": "India", "skillTags": [], "company": null,
             * "school": "Thakur College of Engineering", "starRating": 2, "aboutMe": "CSE TCET'24 | ANDROID DEVELOPER | NIRMAAN HYPERLOOP |3⭐ @codechef","userAvatar": "https://assets.leetcode.com/users/cdhiraj40/avatar_1630407333.png",
             * "reputation": 0, "ranking": 100001 }, "submitStats": { "acSubmissionNum": [ { "difficulty": "All", "count": 95, "submissions": 109 },{ "difficulty": "Easy", "count": 51, "submissions": 58 },
             * { "difficulty": "Medium", "count": 41, "submissions": 47 }, { "difficulty": "Hard", "count": 3, "submissions": 4 } ],"badges": [], "upcomingBadges": [], "activeBadge": null } } }
             */
            var username: String? = null
            var socialAccounts: String? = null
            var githubUrl: String? = null
            var contributions: ContributionsNode? = null
            var submissionCalendar: String? = null
            var profile: ContributionsNode.ProfileNode? = null
            var submitStats: ContributionsNode.ProfileNode.SubmitStatsNode? = null

            class ContributionsNode : Serializable {
                /**
                 * points: 367,
                 * questionCount: 0
                 * testcaseCount: 0
                 * profile: { "realName": "Dhiraj Chauhan",
                 * "websites": [ "https://linktr.ee/cdhiraj40" ],"countryName": "India", "skillTags": [], "company": null, "school": "Thakur College of Engineering", "starRating": 2, "aboutMe": "CSE TCET'24 | ANDROID DEVELOPER | NIRMAAN HYPERLOOP |3⭐ @codechef",
                 * "userAvatar": "https://assets.leetcode.com/users/cdhiraj40/avatar_1630407333.png", "reputation": 0, "ranking": 100001 }, "submitStats": { "acSubmissionNum": [ { "difficulty": "All", "count": 95, "submissions": 109 },
                 * { "difficulty": "Easy", "count": 51, "submissions": 58 }, { "difficulty": "Medium", "count": 41, "submissions": 47 }, { "difficulty": "Hard", "count": 3, "submissions": 4 } ],
                 *  "badges": [], "upcomingBadges": [], "activeBadge": null } } }
                 */
                var points: Int = 0
                var questionCount: Int = 0
                var testcaseCount: Int = 0
                var profile: ProfileNode? = null

                class ProfileNode : Serializable {
                    /**
                     * realName: "Dhiraj Chauhan"
                     * countryName: "India"
                     * company: null
                     * school: "Thakur College of Engineering"
                     * starRating: 2
                     * aboutMe: "CSE TCET'24 | ANDROID DEVELOPER | NIRMAAN HYPERLOOP |3⭐ @codechef"
                     * userAvatar: "https://assets.leetcode.com/users/cdhiraj40/avatar_1630407333.png"
                     * reputation: 0
                     * ranking: 100001
                     * "submitStats": { "acSubmissionNum": [ { "difficulty": "All", "count": 95, "submissions": 109 },
                     * { "difficulty": "Easy", "count": 51, "submissions": 58 }, { "difficulty": "Medium", "count": 41, "submissions": 47 }, { "difficulty": "Hard", "count": 3, "submissions": 4 } ],
                     *  "badges": [], "upcomingBadges": [], "activeBadge": null } } }
                     *  "submitStats": { "acSubmissionNum": [ { "difficulty": "All", "count": 110, "submissions": 127 }, { "difficulty": "Easy", "count": 53, "submissions": 60 }, { "difficulty": "Medium",
                     *  "count": 52, "submissions": 60 }, { "difficulty": "Hard", "count": 5, "submissions": 7 } ], "totalSubmissionNum": [ { "difficulty": "All", "count": 113, "submissions": 249 },
                     *  { "difficulty": "Easy", "count": 54, "submissions": 124 }, { "difficulty": "Medium", "count": 52, "submissions": 109 }, { "difficulty": "Hard", "count": 7, "submissions": 16 } ]
                     */
                    var realName: String? = null
                    var countryName: String? = null
                    var websites: List<String>? = null
                    var skillTags: List<String>? = null
                    var company: String? = null
                    var school: String? = null
                    var starRating: Double? = null
                    var aboutMe: String? = null
                    var userAvatar: String? = null
                    var reputation: Int? = null
                    var ranking: Long? = null

                    class SubmitStatsNode : Serializable {
                        val acSubmissionNum: List<AcSubmissionNumNode>? = null
                        val totalSubmissionNum: List<AcSubmissionNumNode>? = null

                        class AcSubmissionNumNode : Serializable {
                            val count: Int? = null
                            val difficulty: String? = null
                            val submissions: Int? = null
                        }
                    }
                }
            }
        }
    }
}
