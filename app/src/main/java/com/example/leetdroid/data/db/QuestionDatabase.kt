package com.example.leetdroid.data.db

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.leetdroid.data.dao.QuestionsDao
import com.example.leetdroid.data.entitiy.AllQuestions

@Database(entities = [AllQuestions::class], version = 3)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionsDao(): QuestionsDao

    companion object {
        private var INSTANCE: QuestionDatabase? = null
        fun getInstance(context: Context): QuestionDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionDatabase::class.java,
                    "question.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }

}
