package com.example.leetdroid.api

data class LeetCodeRequests(
    val operationName: String,
    val variables: Variables,
    val query: String
) {

    object Helper {

        // request for all general discussions
        val generalDiscussionRequest = fun(limit: Int) = LeetCodeRequests(
            operationName = "categoryTopicList",
            variables = Variables(
                orderBy = "hot",
                query = "",
                skip = 0,
                first = limit,
                categories = listOf("interview-experience"),
            ),
            query = "query categoryTopicList(\$categories: [String!]!, \$first: Int!, \$orderBy: TopicSortingOption, \$skip: Int, \$query: String, \$tags: [String!]) { categoryTopicList" +
                    "(categories: \$categories, orderBy: \$orderBy, skip: \$skip, query: \$query, first: \$first, tags: \$tags) { ...TopicsList } } fragment TopicsList on TopicConnection " +
                    "{ totalNum  edges {  node {  id title commentCount  viewCount  tags { name slug } post { id voteCount creationDate author { username profile { userAvatar } } } } } }"
        )


        val generalDiscussionItemRequest = fun(topicId: Int?) = LeetCodeRequests(
            operationName = "DiscussTopic",
            variables = Variables(
                topicId = topicId,
            ),
            query = "query DiscussTopic(\$topicId: Int!) { topic(id: \$topicId) { id viewCount topLevelCommentCount title pinned tags post { ...DiscussPost } } }" +
                    "fragment DiscussPost on PostNode { id voteCount content updationDate creationDate author { username profile { userAvatar reputation } } } "
        )

        // request for user data
        val getUserProfileRequest = fun(userName: String) = LeetCodeRequests(
            operationName = "getUserProfile",
            variables = Variables(
                username = userName,
                query = "",
            ),
            query = "query getUserProfile(\$username: String!) { allQuestionsCount { difficulty count __typename } matchedUser(username: \$username) { username socialAccounts githubUrl contributions { points questionCount" +
                    " testcaseCount __typename } profile { realName websites countryName skillTags company school starRating aboutMe userAvatar reputation ranking __typename } submissionCalendar submitStats: submitStatsGlobal" +
                    " { acSubmissionNum { difficulty count submissions __typename } totalSubmissionNum { difficulty count submissions __typename } __typename } badges { id displayName icon creationDate medal { slug config " +
                    "{ icon iconGif iconGifBackground iconWearing __typename } __typename } __typename } upcomingBadges { name icon __typename } activeBadge { id __typename } __typename } } "
        )

        // request for all question list
        val getAllQuestionsRequest = fun(categorySlug: String, limit: Int) = LeetCodeRequests(
            operationName = "problemsetQuestionList",
            variables = Variables(
                skip = 0,
                categorySlug = categorySlug,
                limit = limit,
                filters = Filters(tags = listOf())
            ),
            query = "query problemsetQuestionList(\$categorySlug: String, \$limit: Int, \$skip: Int, \$filters: QuestionListFilterInput) { problemsetQuestionList: questionList(    " +
                    "categorySlug: \$categorySlug limit: \$limit skip: \$skip filters: \$filters) {total: totalNum questions: data {acRate difficulty     frontendQuestionId: questionFrontendId  " +
                    "paidOnly: isPaidOnly     title titleSlug topicTags { name id     slug }hasSolution } } }"
        )

        // request for question item data
        val getQuestionContent = fun(titleSlug: String?) = LeetCodeRequests(
            operationName = "questionData",
            variables = Variables(
                titleSlug = titleSlug,
            ),
            query = "query questionData(\$titleSlug: String!) { question(titleSlug: \$titleSlug) { questionId questionFrontendId title titleSlug content isPaidOnly difficulty likes dislikes exampleTestcases categoryTitle " +
                    "topicTags { name slug translatedName } stats hints solution { id canSeeDetail paidOnly hasVideoSolution paidOnlyVideo } } }  "
        )

        // request for question item solution
        val getQuestionSolution = fun(titleSlug: String) = LeetCodeRequests(
            operationName = "QuestionNote",
            variables = Variables(
                titleSlug = titleSlug
            ),
            query = "query QuestionNote(\$titleSlug: String!) { question(titleSlug: \$titleSlug) { questionId article solution { id content contentTypeId canSeeDetail paidOnly hasVideoSolution " +
                    "paidOnlyVideo rating { id count average userRating { score } } } } } "
        )

        // request for question item all discussion list
        val getQuestionDiscussions = fun(questionId: String, orderBy: String?, limit: Int) =
            LeetCodeRequests(
                operationName = "questionTopicsList",
                variables = Variables(
                    skip = 0,
                    orderBy = orderBy,
                    first = limit,
                    questionId = questionId,
                    query = "",
                    tags = listOf()
                ),
                query = "query questionTopicsList(\$questionId: String!, \$orderBy: TopicSortingOption, \$skip: Int, \$query: String, \$first: Int!, \$tags: [String!]) { questionTopicsList(questionId: \$questionId, " +
                        "orderBy: \$orderBy, skip: \$skip, query: \$query, first: \$first, tags: \$tags) { ...TopicsList } } fragment TopicsList on TopicConnection { totalNum edges { node { id title commentCount " +
                        "viewCount tags { name slug } post { id voteCount creationDate author { username isActive nameColor activeBadge { displayName icon } profile { userAvatar } } status } } } }  "
            )

        // request for question item's  discussion item
        val getQuestionDiscussionItem = fun(questionId: Int?) = LeetCodeRequests(
            operationName = "DiscussTopic",
            variables = Variables(
                topicId = questionId,
                query = "",
            ),
            query = "query DiscussTopic(\$topicId: Int!) { topic(id: \$topicId) { id viewCount title pinned tags post { ...DiscussPost } } }" +
                    "fragment DiscussPost on PostNode { id voteCount content updationDate creationDate author { username profile { userAvatar reputation } } }"
        )

        // request for upcoming contests not being used
        val upcomingContestRequest = LeetCodeRequests(
            operationName = "getUserProfile",
            variables = Variables(
            ),
            query = "query upcomingContests { upcomingContests { title titleSlug startTime duration } } "
        )

        val getDailyQuestion = LeetCodeRequests(
            operationName = "",
            variables = Variables(),
            query = "query questionOfToday { activeDailyCodingChallengeQuestion { date userStatus link question { acRate difficulty freqBar frontendQuestionId: " +
                    "questionFrontendId isFavor paidOnly: isPaidOnly status title titleSlug hasVideoSolution hasSolution topicTags { name id slug } } } } "

        )

        // request for random question
        val getRandomQuestion = LeetCodeRequests(
            operationName = "randomQuestion",
            variables = Variables(
                categorySlug = "",
                filters = Filters(orderBy = "FRONTEND_ID"),
            ),
            query = "query randomQuestion(\$categorySlug: String, \$filters: QuestionListFilterInput) {  randomQuestion(categorySlug: \$categorySlug, filters: \$filters) {    titleSlug  }}"
        )
    }

    data class Variables(
        val username: String? = null,
        val titleSlug: String? = null,
        val categorySlug: String? = null,
        val orderBy: String? = null,
        val query: String? = null,
        val skip: Int? = null,
        val first: Int? = null,
        val categories: List<String>? = null,
        val filters: Filters? = null,
        val questionId: String? = null,
        val topicId: Int? = null,
        val tags: List<String>? = null,
        val limit: Int? = null
    )

    data class Filters(
        val tags: List<String>? = null,
        val orderBy: String? = null
    )
}