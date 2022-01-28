package com.example.leetdroid.data.entitiy

import androidx.room.*
import com.example.leetdroid.model.UserProfileModel
import com.example.leetdroid.utils.Converters

@Entity(tableName = "user_profile")
@TypeConverters(
    Converters.ArrayListConverters::class,
    Converters.MatchedUserNodeConverters::class,
    Converters.SubmitStatsNodeConverters::class,
    Converters.ContributionsNodeConverters::class,
    Converters.ProfileNodeConverters::class
)
data class User(
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