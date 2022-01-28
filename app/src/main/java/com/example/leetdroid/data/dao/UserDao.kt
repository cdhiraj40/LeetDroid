package com.example.leetdroid.data.dao

import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.User

@Dao
interface UserDao {

    @Insert
    fun insert(model: User?)

    @Update
    fun update(model: User?)

    @Query("SELECT * FROM user_profile  WHERE id=:id ")
    suspend fun getUser(id:Int): User
}