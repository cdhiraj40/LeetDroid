package com.cdhiraj40.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.cdhiraj40.leetdroid.data.entitiy.DailyQuestion

@Dao
interface DailyQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(user: DailyQuestion?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestion(user: DailyQuestion?)

    @Query("DELETE FROM daily_question WHERE id=:id")
    suspend fun deleteQuestion(id: Int)

    @Query("SELECT * FROM daily_question WHERE id=:id ")
    fun getQuestion(id:Int): LiveData<DailyQuestion>
}