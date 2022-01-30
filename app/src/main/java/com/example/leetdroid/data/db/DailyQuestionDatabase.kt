package com.example.leetdroid.data.db

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.leetdroid.data.dao.DailyQuestionDao
import com.example.leetdroid.data.entitiy.DailyQuestion

@Database(entities = [DailyQuestion::class], version = 2)
abstract class DailyQuestionDatabase : RoomDatabase() {

    abstract fun dailyQuestionDao(): DailyQuestionDao

    companion object {
        private var INSTANCE: DailyQuestionDatabase? = null
        fun getInstance(context: Context): DailyQuestionDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    DailyQuestionDatabase::class.java,
                    "daily_question.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }

}
