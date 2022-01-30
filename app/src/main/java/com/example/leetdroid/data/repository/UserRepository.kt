package com.example.leetdroid.data.repository

import androidx.lifecycle.LiveData
import com.example.leetdroid.data.dao.UserDao
import com.example.leetdroid.data.entitiy.User

class UserRepository(private val userDao: UserDao) {

    val user = fun(id:Int):LiveData<User> = userDao.getUser(id)

    suspend fun insertUser(user: User) =
        userDao.insertUser(user)

    suspend fun updateUser(user: User) =
        userDao.updateUser(user)

    suspend fun deleteUser(user: User) =
        userDao.deleteUser(user)
}