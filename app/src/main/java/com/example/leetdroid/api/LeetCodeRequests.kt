package com.example.leetdroid.api

data class LeetCodeRequests(
    val operationName: String,
    val variables: Variables,
    val query: String
) {

    object Helper {
        //    private val limit = 10
        val generalDiscussionRequest = fun(limit: Int) = LeetCodeRequests(
            operationName = "categoryTopicList",
            variables = Variables(
                orderBy = "hot",
                query = "",
                skip = 0,
                first = limit,
                categories = listOf("interview-experience"),
            ),
            query = "query categoryTopicList(\$categories: [String!]!, \$first: Int!, \$orderBy: TopicSortingOption, \$skip: Int, \$query: String, \$tags: [String!]) { categoryTopicList(categories:" +
                    " \$categories, orderBy: \$orderBy, skip: \$skip, query: \$query, first: \$first, tags: \$tags) { ...TopicsList } } fragment TopicsList on TopicConnection { totalNum  edges {  node {" +
                    "  id title commentCount  viewCount  tags { name slug } post { id voteCount creationDate author { username profile { userAvatar } } } } } }"
        )

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

        val getAllQuestionsRequest = fun(categorySlug: String, limit: Int) = LeetCodeRequests(
            operationName = "problemsetQuestionList",
            variables = Variables(
                skip = 0,
                categorySlug = categorySlug,
                first = limit,
                filters = Filters(tags = listOf())
            ),
            query = "query problemsetQuestionList(\$categorySlug: String, \$limit: Int, \$skip: Int, \$filters: QuestionListFilterInput) { problemsetQuestionList: questionList(    " +
                    "categorySlug: \$categorySlug limit: \$limit skip: \$skip filters: \$filters) {total: totalNum questions: data {acRate difficulty     frontendQuestionId: questionFrontendId  " +
                    "paidOnly: isPaidOnly     title titleSlug topicTags { name id     slug }hasSolution } } }"
        )

        val getQuestionContent = fun(titleSlug: String?) = LeetCodeRequests(
            operationName = "questionData",
            variables = Variables(
                titleSlug = titleSlug,
            ),
            query = "query questionData(\$titleSlug: String!) { question(titleSlug: \$titleSlug) { questionId questionFrontendId title titleSlug content difficulty likes dislikes exampleTestcases categoryTitle " +
                    "topicTags { name slug translatedName } stats hints } }  "
        )

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

        val getQuestionDiscussionItem = fun(questionId: Int?) = LeetCodeRequests(
            operationName = "DiscussTopic",
            variables = Variables(
                topicId = questionId,
                query = "",
            ),
            query = "query DiscussTopic(\$topicId: Int!) { topic(id: \$topicId) { id viewCount title pinned tags post { ...DiscussPost } } }" +
                    "fragment DiscussPost on PostNode { id voteCount content updationDate creationDate author { username profile { userAvatar reputation } } }"
        )

        val upcomingContestRequest = LeetCodeRequests(
            operationName = "getUserProfile",
            variables = Variables(
            ),
            query = "query upcomingContests { upcomingContests { title titleSlug startTime duration } } "
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
        val tags : List<String>? = null
    )

    data class Filters(
        val tags: List<String>
    )
}