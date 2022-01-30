package com.example.leetdroid.data.db

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.leetdroid.data.dao.ContestDao

import com.example.leetdroid.data.entitiy.Contest

@Database(entities = [Contest::class], version = 5)
abstract class ContestsDatabase : RoomDatabase() {

    abstract fun contestDao(): ContestDao

    companion object {
        private var INSTANCE: ContestsDatabase? = null
        fun getInstance(context: Context): ContestsDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ContestsDatabase::class.java,
                    "contests.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }

}
