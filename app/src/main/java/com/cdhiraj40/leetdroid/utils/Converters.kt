package com.cdhiraj40.leetdroid.utils

import androidx.room.TypeConverter
import com.cdhiraj40.leetdroid.data.entitiy.Contest
import com.cdhiraj40.leetdroid.model.DailyQuestionModel
import com.cdhiraj40.leetdroid.model.UserProfileModel
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {

    object DifficultyEnumConverters {

        @TypeConverter
        fun toDifficulty(value: String) = enumValueOf<Constant.DIFFICULTY>(value)

        @TypeConverter
        fun fromDifficulty(value: Constant.DIFFICULTY) = value.name
    }

    object ArrayListConverters {
        @TypeConverter
        fun fromString(value: String?): ArrayList<String?>? {
            val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list: List<String?>?): String? {
            val gson = Gson()
            return gson.toJson(list)
        }
    }

    object AllQuestionsCountConverters {
        @TypeConverter
        fun fromStringAllQuestionsCount(value: String): List<UserProfileModel.DataNode.AllQuestionsCountNode>? {
            val listType: Type =
                object : TypeToken<List<UserProfileModel.DataNode.AllQuestionsCountNode>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromAllQuestionsCountNode(jsonObject: List<UserProfileModel.DataNode.AllQuestionsCountNode>?): String? {
            val gson = Gson()
            return gson.toJson(jsonObject)
        }
    }

    object MatchedUserNodeConverters {
        @TypeConverter
        fun fromStringMatchedUser(value: String): UserProfileModel.DataNode.MatchedUserNode? {
            val listType: Type =
                object : TypeToken<UserProfileModel.DataNode.MatchedUserNode?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromMatchedUserNode(jsonObject: UserProfileModel.DataNode.MatchedUserNode?): String? {
            val gson = Gson()
            return gson.toJson(jsonObject)
        }
    }

    object ContributionsNodeConverters {
        @TypeConverter
        fun fromStringContributions(value: String): UserProfileModel.DataNode.MatchedUserNode.ContributionsNode {
            val listType: Type = object :
                TypeToken<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromContributionsNode(jsonObject: UserProfileModel.DataNode.MatchedUserNode.ContributionsNode): String? {
            val gson = Gson()
            return gson.toJson(jsonObject)
        }
    }

    object ProfileNodeConverters {
        @TypeConverter
        fun fromStringProfileNode(value: String): UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode? {
            val listType: Type = object :
                TypeToken<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromProfileNode(jsonObject: UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode): String? {
            val gson = Gson()
            return gson.toJson(jsonObject)
        }
    }

    object SubmitStatsNodeConverters {
        @TypeConverter
        fun fromStringSubmitStats(value: String): List<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode.SubmitStatsNode.AcSubmissionNumNode>? {
            val listType: Type = object :
                TypeToken<List<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode.SubmitStatsNode.AcSubmissionNumNode>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromSubmitStatsNode(submitStats: List<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode.SubmitStatsNode.AcSubmissionNumNode>): String? {
            val gson = Gson()
            return gson.toJson(submitStats)
        }
    }

    object ContestConverter {
        @TypeConverter
        fun fromStringContest(value: String): List<Contest>? {
            val listType: Type = object : TypeToken<List<Contest>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromContest(contests: List<Contest>): String? {
            val gson = Gson()
            return gson.toJson(contests)
        }
    }

    object DailyQuestionDailyConverter {
        @TypeConverter
        fun fromStringDailyQuestionDaily(value: String): DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode? {
            val listType: Type = object :
                TypeToken<DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromDailyQuestionDaily(activeDailyChallenge: DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode): String? {
            val gson = Gson()
            return gson.toJson(activeDailyChallenge)
        }
    }

    object DailyQuestionConverter {
        @TypeConverter
        fun fromStringDailyQuestion(value: String): DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode.QuestionNode? {
            val listType: Type = object :
                TypeToken<DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode.QuestionNode?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromDailyQuestion(question: DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode.QuestionNode): String? {
            val gson = Gson()
            return gson.toJson(question)
        }
    }

    object DailyQuestionTagsConverter {
        @TypeConverter
        fun fromStringDailyQuestionTags(value: String): List<DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode.QuestionNode.TopicTagsNode>? {
            val listType: Type = object :
                TypeToken<List<DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode.QuestionNode.TopicTagsNode>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromDailyQuestionTags(tags: List<DailyQuestionModel.DataNode.ActiveDailyCodingChallengeQuestionNode.QuestionNode.TopicTagsNode>): String? {
            val gson = Gson()
            return gson.toJson(tags)
        }
    }
}