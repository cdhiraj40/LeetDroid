package com.example.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.Contest

@Dao
interface ContestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContest(model: Contest?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContest(model: Contest?)

    @Delete
    suspend fun deleteContest(contest: Contest?)

    @Query("SELECT * FROM contests")
    fun getAllContest(): LiveData<List<Contest>>

    @Query("SELECT * FROM contests  WHERE id=:id ")
    suspend fun getContest(id: Int): Contest
}