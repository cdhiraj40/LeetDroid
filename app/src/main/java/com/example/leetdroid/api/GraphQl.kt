package com.example.leetdroid.api

object GraphQl {

    const val GET_USER_PROFILE =
        "{\"operationName\":\"getUserProfile\", \"variables\":{\"username\":\"%s\"},\"query\":\"query getUserProfile(\$username: String!) { matchedUser(username: \$username) { username socialAccounts githubUrl contributions { points      questionCount      testcaseCount          }    profile {      realName      websites      countryName      skillTags      company      school      starRating      aboutMe      userAvatar      reputation      ranking          }    submissionCalendar    submitStats: submitStatsGlobal {      acSubmissionNum {        difficulty        count        submissions              }      totalSubmissionNum {        difficulty        count        submissions              }          }    badges {      id      displayName      icon      creationDate      medal {        slug        config {          icon          iconGif          iconGifBackground          iconWearing                  }              }          }    upcomingBadges {      name      icon          }    activeBadge {      id          }      }}\"}"

    const val UPCOMING_CONTEST =
        "{\"operationName\":\"upcomingContests\",\"variables\":{},\"query\":\"query upcomingContests {  upcomingContests {    title    titleSlug    startTime    duration   }}\"}"

    const val ALL_QUESTION_LIST = "{\"operationName\": \"problemsetQuestionList\",\"variables\"  :{\"categorySlug\":\"%s\", \"skip\": %d,\"limit\": %d, \"filters\":{} },\"query\": \"query problemsetQuestionList(\$categorySlug: String, \$limit: Int, \$skip: Int, \$filters: QuestionListFilterInput) { problemsetQuestionList: questionList(    categorySlug: \$categorySlug limit: \$limit skip: \$skip filters: \$filters) {total: totalNum questions: data {acRate difficulty     frontendQuestionId: questionFrontendId     paidOnly: isPaidOnly     title titleSlug topicTags { name id     slug } hasSolution } } }\" }"

    const val QUESTION_ITEM_DATA = "{\"operationName\": \"questionData\",\"variables\"  :{\"titleSlug\":\"%s\"},\"query\": \"query questionData(\$titleSlug: String!) { question(titleSlug: \$titleSlug) { questionId questionFrontendId title titleSlug content difficulty likes dislikes exampleTestcases categoryTitle topicTags { name slug  }stats hints } }\" }"

    const val QUESTION_DISCUSSION =
        "{\"operationName\": \"questionTopicsList\",\"variables\":{\"orderBy\": \"%s\",\"query\": \"\",\"skip\": %d,\"first\": %d,\"questionId\": \"%s\"},\"query\": \"query questionTopicsList(\$questionId: String!, \$orderBy: TopicSortingOption, \$skip: Int, \$query: String, \$first: Int!) {  questionTopicsList(questionId: \$questionId, orderBy: \$orderBy, skip: \$skip, query: \$query, first: \$first) {    ...TopicsList     }}fragment TopicsList on TopicConnection {  totalNum  edges {    node {      id      title      viewCount      post {        id        voteCount        creationDate        author {          username          profile {            userSlug            userAvatar          }}}}}}\"}"

}


