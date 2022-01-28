package com.example.leetdroid.data.entitiy

import androidx.room.*
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.Converters

@Entity(tableName = "all_questions")
@TypeConverters(Converters.DifficultyEnumConverters::class)
data class AllQuestions(
    @ColumnInfo(name = "acRate")
    val acRate: Double,
    @ColumnInfo(name = "difficulty")
    val difficulty: Constant.DIFFICULTY, //enum converter
    @ColumnInfo(name = "frontendQuestionId")
    val frontendQuestionId: String,
    @ColumnInfo(name = "paidOnly")
    val paidOnly: Boolean,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "titleSlug")
    val titleSlug: String,
//    @ColumnInfo(name = "topicTags")
//    val topicTags: String, // one to many
    @ColumnInfo(name = "hasSolution")
    val hasSolution: Boolean,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
