package com.example.leetdroid.data.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.leetdroid.utils.Converters

@TypeConverters(
    Converters.DailyQuestionConverter::class,
    Converters.DailyQuestionDailyConverter::class,
    Converters.DailyQuestionTagsConverter::class
)
@Entity(tableName = "daily_question")
data class DailyQuestion(
    @ColumnInfo(name = "activeDailyCodingChallengeQuestion")
    val activeDailyCodingChallengeQuestion: String,

    @ColumnInfo(name = "question")
    val question: String,

    @ColumnInfo(name = "topicTags")
    val topicTags: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
