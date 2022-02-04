package com.example.leetdroid.model

import java.io.Serializable

/**
 *  Data Model for the all questions list
 */
class RecentSubmissionsModel : Serializable {

    /**
     *  "data":
     *  "recentSubmissionList": [ { "title": "Minimum Remove to Make Valid Parentheses", "titleSlug": "minimum-remove-to-make-valid-parentheses", "timestamp": "1605157667",
     * "statusDisplay": "Accepted", "lang": "cpp" } ], "languageList": [ { "id": 0, "name": "cpp", "verboseName": "C++" }, { "id": 1, "name": "java", "verboseName": "Java" },
     * { "id": 2, "name": "python", "verboseName": "Python" }, { "id": 11, "name": "python3", "verboseName": "Python3" }, { "id": 3, "name": "mysql", "verboseName": "MySQL" },
     * { "id": 14, "name": "mssql", "verboseName": "MS SQL Server" }, { "id": 15, "name": "oraclesql", "verboseName": "Oracle" }, { "id": 4, "name": "c", "verboseName": "C" },
     * { "id": 5, "name": "csharp", "verboseName": "C#" }, { "id": 6, "name": "javascript", "verboseName": "JavaScript" }, { "id": 7, "name": "ruby", "verboseName": "Ruby" },
     * { "id": 8, "name": "bash", "verboseName": "Bash" }, { "id": 9, "name": "swift", "verboseName": "Swift" }, { "id": 10, "name": "golang", "verboseName": "Go" },
     * { "id": 12, "name": "scala", "verboseName": "Scala" }, { "id": 16, "name": "html", "verboseName": "HTML" }, { "id": 17, "name": "pythonml", "verboseName": "Python ML" },
     * { "id": 13, "name": "kotlin", "verboseName": "Kotlin" }, { "id": 18, "name": "rust", "verboseName": "Rust" }, { "id": 19, "name": "php", "verboseName": "PHP" },
     * { "id": 20, "name": "typescript", "verboseName": "TypeScript" }, { "id": 21, "name": "racket", "verboseName": "Racket" }, { "id": 22, "name": "erlang", "verboseName": "Erlang" },
     * { "id": 23, "name": "elixir", "verboseName": "Elixir" } ] } }
     */
    var data: DataNode? = null

    class DataNode : Serializable {
        /**
         * "recentSubmissionList":
         * "title": "Minimum Remove to Make Valid Parentheses",
         * "titleSlug": "minimum-remove-to-make-valid-parentheses",
         * "timestamp": "1605157667",
         * "statusDisplay": "Accepted",
         * "lang": "cpp" ,
         * "languageList": [ { "id": 0, "name": "cpp", "verboseName": "C++" }, { "id": 1, "name": "java", "verboseName": "Java" },
         * { "id": 2, "name": "python", "verboseName": "Python" }, { "id": 11, "name": "python3", "verboseName": "Python3" }, { "id": 3, "name": "mysql", "verboseName": "MySQL" },
         * { "id": 14, "name": "mssql", "verboseName": "MS SQL Server" }, { "id": 15, "name": "oraclesql", "verboseName": "Oracle" }, { "id": 4, "name": "c", "verboseName": "C" },
         * { "id": 5, "name": "csharp", "verboseName": "C#" }, { "id": 6, "name": "javascript", "verboseName": "JavaScript" }, { "id": 7, "name": "ruby", "verboseName": "Ruby" },
         * { "id": 8, "name": "bash", "verboseName": "Bash" }, { "id": 9, "name": "swift", "verboseName": "Swift" }, { "id": 10, "name": "golang", "verboseName": "Go" },
         * { "id": 12, "name": "scala", "verboseName": "Scala" }, { "id": 16, "name": "html", "verboseName": "HTML" }, { "id": 17, "name": "pythonml", "verboseName": "Python ML" },
         * { "id": 13, "name": "kotlin", "verboseName": "Kotlin" }, { "id": 18, "name": "rust", "verboseName": "Rust" }, { "id": 19, "name": "php", "verboseName": "PHP" },
         * { "id": 20, "name": "typescript", "verboseName": "TypeScript" }, { "id": 21, "name": "racket", "verboseName": "Racket" }, { "id": 22, "name": "erlang", "verboseName": "Erlang" },
         * { "id": 23, "name": "", "verboseName": "Elixir" } ] } }
         */

        var recentSubmissionList: List<RecentSubmissionListNode>? = null

        class RecentSubmissionListNode : Serializable {

            var title: String? = null
            var titleSlug: String? = null
            var timestamp: String? = null
            var statusDisplay: String? = null
            var lang: String? = null
            var languageList: List<LanguageListNode>? = null

            class LanguageListNode : Serializable {

                /**
                 * "languageList"
                 * "id": 0, "name": "cpp", "verboseName": "C++" }, { "id": 1, "name": "java", "verboseName": "Java" },
                 * { "id": 2, "name": "python", "verboseName": "Python" }, { "id": 11, "name": "python3", "verboseName": "Python3" }, { "id": 3, "name": "mysql", "verboseName": "MySQL" },
                 * { "id": 14, "name": "mssql", "verboseName": "MS SQL Server" }, { "id": 15, "name": "oraclesql", "verboseName": "Oracle" }, { "id": 4, "name": "c", "verboseName": "C" },
                 * { "id": 5, "name": "csharp", "verboseName": "C#" }, { "id": 6, "name": "javascript", "verboseName": "JavaScript" }, { "id": 7, "name": "ruby", "verboseName": "Ruby" },
                 * { "id": 8, "name": "bash", "verboseName": "Bash" }, { "id": 9, "name": "swift", "verboseName": "Swift" }, { "id": 10, "name": "golang", "verboseName": "Go" },
                 * { "id": 12, "name": "scala", "verboseName": "Scala" }, { "id": 16, "name": "html", "verboseName": "HTML" }, { "id": 17, "name": "pythonml", "verboseName": "Python ML" },
                 * { "id": 13, "name": "kotlin", "verboseName": "Kotlin" }, { "id": 18, "name": "rust", "verboseName": "Rust" }, { "id": 19, "name": "php", "verboseName": "PHP" },
                 * { "id": 20, "name": "typescript", "verboseName": "TypeScript" }, { "id": 21, "name": "racket", "verboseName": "Racket" }, { "id": 22, "name": "erlang", "verboseName": "Erlang" },
                 * { "id": 23, "name": "elixir", "verboseName": "Elixir" } ] } }
                 */

                var id: Int? = null
                var name: String? = null
                var verboseName: String? = null
            }
        }
    }
}