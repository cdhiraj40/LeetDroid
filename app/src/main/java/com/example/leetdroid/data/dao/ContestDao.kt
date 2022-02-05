package com.example.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.leetdroid.data.entitiy.Contest

@Dao
interface ContestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContest(model: Contest?): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContest(model: Contest?)

    @Query("DELETE FROM contests WHERE id=:id")
    suspend fun deleteContest(id: Int)

    @Query("SELECT * FROM contests")
    fun getAllContest(): LiveData<List<Contest>>

    @Query("SELECT * FROM contests  WHERE id=:id ")
    suspend fun getContest(id: Int): Contest
}