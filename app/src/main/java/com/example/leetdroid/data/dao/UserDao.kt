package com.example.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.leetdroid.data.entitiy.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User?)

    @Query("DELETE FROM user_profile WHERE id=:id")
    suspend fun deleteUser(id: Int)

    @Query("SELECT * FROM user_profile WHERE id=:id ")
    fun getUser(id: Int): LiveData<User>
}