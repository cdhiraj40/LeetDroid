package com.example.leetdroid.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.leetdroid.data.db.ContestsDatabase

import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.data.repository.ContestRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContestViewModel(application: Application) : AndroidViewModel(application) {

    private val contestRepository: ContestRepository
//    val weeklyContest: Contest
//    val biweeklyContest: Contest
    val getAllContest: LiveData<List<Contest>>

    init {
        val contestDB = ContestsDatabase.getInstance(application).contestDao()
        contestRepository = ContestRepository(contestDB)
//        biweeklyContest = contestRepository.getContest(1)
//        weeklyContest = contestRepository.contest(2)
        getAllContest = contestRepository.allContest
    }

    fun addContest(contest: Contest) {
        viewModelScope.launch(Dispatchers.IO) {
            contestRepository.insertContest(contest)
        }
    }

    fun updateContest(contest: Contest) {
        viewModelScope.launch(Dispatchers.IO) {
            contestRepository.updateContest(contest)
        }
    }

    fun deleteContest(contest: Contest) {
        viewModelScope.launch(Dispatchers.IO) {
            contestRepository.deleteContest(contest)
        }
    }

    suspend fun getContest(id:Int):Contest{
        viewModelScope.launch(Dispatchers.IO) {
            contestRepository.getContest(id)
        }
        return contestRepository.getContest(id)
    }
}
