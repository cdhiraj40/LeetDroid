package com.example.leetdroid.data.repository

import androidx.lifecycle.LiveData
import com.example.leetdroid.data.dao.DailyQuestionDao
import com.example.leetdroid.data.entitiy.DailyQuestion

class DailyQuestionRepository(private val dailyQuestionDao: DailyQuestionDao) {

    val todaysQuestion = fun(id: Int): LiveData<DailyQuestion> = dailyQuestionDao.getQuestion(id)

    suspend fun insertQuestion(question: DailyQuestion) =
        dailyQuestionDao.insertQuestion(question)

    suspend fun updateQuestion(question: DailyQuestion) =
        dailyQuestionDao.updateQuestion(question)

    suspend fun deleteQuestion(id: Int) =
        dailyQuestionDao.deleteQuestion(id)
}