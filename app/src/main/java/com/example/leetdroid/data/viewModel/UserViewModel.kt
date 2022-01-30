package com.example.leetdroid.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.leetdroid.data.db.UserDatabase
import com.example.leetdroid.data.entitiy.User
import com.example.leetdroid.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    val getUser: LiveData<User>
    val allUsers: LiveData<List<User>>


    init {
        val userDB = UserDatabase.getInstance(application).userDao()
        userRepository = UserRepository(userDB)
        getUser = userRepository.user(1)
        allUsers = userRepository.allUsers
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun deleteAllUserDate(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
    }
}
