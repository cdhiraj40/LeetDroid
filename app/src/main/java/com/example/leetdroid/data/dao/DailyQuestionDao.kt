package com.example.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.DailyQuestion

@Dao
interface DailyQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(user: DailyQuestion?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestion(user: DailyQuestion?)

    @Delete
    suspend fun deleteQuestion(user: DailyQuestion?)

    @Query("SELECT * FROM daily_question WHERE id=:id ")
    fun getQuestion(id:Int): LiveData<DailyQuestion>
}