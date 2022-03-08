package com.cdhiraj40.leetdroid.data.repository

import androidx.lifecycle.LiveData
import com.cdhiraj40.leetdroid.data.dao.UserDao
import com.cdhiraj40.leetdroid.data.entitiy.User

class UserRepository(private val userDao: UserDao) {

    val user = fun(id: Int): LiveData<User> = userDao.getUser(id)

    suspend fun insertUser(user: User) =
        userDao.insertUser(user)

    suspend fun updateUser(user: User) =
        userDao.updateUser(user)

    suspend fun deleteUser(id: Int) =
        userDao.deleteUser(id)
}