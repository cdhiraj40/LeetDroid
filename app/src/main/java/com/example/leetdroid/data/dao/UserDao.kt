package com.example.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User?)

    @Delete
    suspend fun deleteUser(user: User?)

    @Query("SELECT * FROM user_profile WHERE id=:id ")
    fun getUser(id:Int): LiveData<User>

    @Query("SELECT * FROM user_profile ")
    fun getALLUser(): LiveData<List<User>>
}