package com.example.leetdroid.data.dao

import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.AllQuestions

@Dao
interface QuestionsDao {

    @Insert
    fun insert(model: AllQuestions?)

    @Update
    fun update(model: AllQuestions?)


    @Query("SELECT * FROM all_questions")
    suspend fun getAllQuestions():List<AllQuestions>
}