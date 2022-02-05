package com.example.leetdroid.data.repository

import androidx.lifecycle.LiveData
import com.example.leetdroid.data.dao.FirebaseUserDao
import com.example.leetdroid.data.entitiy.FirebaseUserProfile

class FirebaseUserRepository(private val firebaseUserDao: FirebaseUserDao) {

    val firebaseUser = fun(id: Int): LiveData<FirebaseUserProfile> = firebaseUserDao.getUser(id)

    suspend fun insertUser(firebaseUser: FirebaseUserProfile) =
        firebaseUserDao.insertUser(firebaseUser)

    suspend fun updateUser(firebaseUser: FirebaseUserProfile) =
        firebaseUserDao.updateUser(firebaseUser)

    suspend fun deleteUser(id: Int) =
        firebaseUserDao.deleteUser(id)
}