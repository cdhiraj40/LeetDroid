package com.cdhiraj40.leetdroid.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cdhiraj40.leetdroid.data.db.FirebaseUserDatabase
import com.cdhiraj40.leetdroid.data.entitiy.FirebaseUserProfile
import com.cdhiraj40.leetdroid.data.repository.FirebaseUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseUserViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseUserRepository: FirebaseUserRepository
    val getFirebaseUser: LiveData<FirebaseUserProfile>

    init {
        val firebaseUserDB = FirebaseUserDatabase.getInstance(application).firebaseUserDao()
        firebaseUserRepository = FirebaseUserRepository(firebaseUserDB)
        getFirebaseUser = firebaseUserRepository.firebaseUser(1)
    }

    fun addUser(firebaseUser: FirebaseUserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserRepository.insertUser(firebaseUser)
        }
    }

    fun updateUser(firebaseUser: FirebaseUserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserRepository.updateUser(firebaseUser)
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserRepository.deleteUser(id)
        }
    }
}
