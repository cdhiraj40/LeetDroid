package com.example.leetdroid.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.FirebaseUserProfile

@Dao
interface FirebaseUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userProfile: FirebaseUserProfile?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(userProfile: FirebaseUserProfile?)

    @Query("DELETE FROM firebase_user WHERE id=:id")
    suspend fun deleteUser(id: Int)

    @Query("SELECT * FROM firebase_user WHERE id=:id ")
    fun getUser(id:Int): LiveData<FirebaseUserProfile>
}