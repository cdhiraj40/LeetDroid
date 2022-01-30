package com.example.leetdroid.data.dao

import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.data.entitiy.User

@Dao
interface ContestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: Contest?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(model: Contest?)

    @Query("DELETE  FROM contests")
    suspend fun deleteAllContest()

    @Query("SELECT * FROM contests  WHERE id=:id ")
    suspend fun getContest(id:Int): Contest
}