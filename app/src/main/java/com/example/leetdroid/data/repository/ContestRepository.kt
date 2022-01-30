package com.example.leetdroid.data.repository

import androidx.lifecycle.LiveData
import com.example.leetdroid.data.dao.ContestDao
import com.example.leetdroid.data.entitiy.Contest

class ContestRepository(private val contestDao: ContestDao) {


    val allContest: LiveData<List<Contest>> = contestDao.getAllContest()


    suspend fun insertContest(contest: Contest) =
        contestDao.insertContest(contest)

    suspend fun updateContest(contest: Contest) =
        contestDao.updateContest(contest)

    suspend fun deleteContest(contest: Contest) =
        contestDao.deleteContest(contest)

    suspend fun getContest(id: Int):Contest =
        contestDao.getContest(id)
}