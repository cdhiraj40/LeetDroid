package com.cdhiraj40.leetdroid.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cdhiraj40.leetdroid.data.db.DailyQuestionDatabase

import com.cdhiraj40.leetdroid.data.entitiy.DailyQuestion

import com.cdhiraj40.leetdroid.data.repository.DailyQuestionRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyQuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyQuestionRepository: DailyQuestionRepository
    val dailyQuestion: LiveData<DailyQuestion>

    init {
        val questionDB = DailyQuestionDatabase.getInstance(application).dailyQuestionDao()
        dailyQuestionRepository = DailyQuestionRepository(questionDB)
        dailyQuestion = dailyQuestionRepository.todaysQuestion(1)
    }

    fun addQuestion(question: DailyQuestion) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyQuestionRepository.insertQuestion(question)
        }
    }

    fun updateQuestion(question: DailyQuestion) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyQuestionRepository.updateQuestion(question)
        }
    }

    fun deleteQuestion(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyQuestionRepository.deleteQuestion(id)
        }
    }
}
