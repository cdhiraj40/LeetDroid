package com.cdhiraj40.leetdroid.data.entitiy

import androidx.room.*
import com.cdhiraj40.leetdroid.utils.Converters

@Entity(tableName = "user_profile")
@TypeConverters(
    Converters.ArrayListConverters::class,
    Converters.MatchedUserNodeConverters::class,
    Converters.SubmitStatsNodeConverters::class,
    Converters.ContributionsNodeConverters::class,
    Converters.ProfileNodeConverters::class,
    Converters.AllQuestionsCountConverters::class
)
data class User(
    @ColumnInfo(name = "allQuestionsCount")
    val allQuestionsCount: String,
    @ColumnInfo(name = "matchedUser")
    val matchedUser: String,
    @ColumnInfo(name = "contributions")
    val contributions: String,
    @ColumnInfo(name = "profile")
    val profile: String,
    @ColumnInfo(name = "acSubmissionNum")
    val acSubmissionNum: String,
    @ColumnInfo(name = "totalSubmissionNum")
    val totalSubmissionNum: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}