package com.example.leetdroid.utils

import androidx.room.TypeConverter
import com.example.leetdroid.model.UserProfileModel
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

    object MatchedUserNodeConverters {
        @TypeConverter
        fun fromStringMatchedUser(value: String): UserProfileModel.DataNode.MatchedUserNode? {
            val listType: Type = object : TypeToken<UserProfileModel.DataNode.MatchedUserNode?>() {}.type
            return Gson().fromJson(value,listType)
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
            val listType: Type = object : TypeToken<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode>() {}.type
            return Gson().fromJson(value,listType)
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
            val listType: Type = object : TypeToken<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode>() {}.type
            return Gson().fromJson(value,listType)
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
            val listType: Type = object : TypeToken<List<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode.SubmitStatsNode.AcSubmissionNumNode>?>() {}.type
            return Gson().fromJson(value,listType)
        }

        @TypeConverter
        fun fromSubmitStatsNode(submitStats: List<UserProfileModel.DataNode.MatchedUserNode.ContributionsNode.ProfileNode.SubmitStatsNode.AcSubmissionNumNode>): String? {
            val gson = Gson()
            return gson.toJson(submitStats)
        }
    }
}