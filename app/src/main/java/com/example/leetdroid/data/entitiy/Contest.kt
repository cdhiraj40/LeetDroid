package com.example.leetdroid.data.entitiy

import androidx.room.*

@Entity(tableName = "contests")
data class Contest(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "start_time")
    val start_time: String,
    @ColumnInfo(name = "end_time")
    val end_time: String,
    @ColumnInfo(name = "in_24_hours")
    val in_24_hours: String,
    @ColumnInfo(name = "status")
    val status: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}