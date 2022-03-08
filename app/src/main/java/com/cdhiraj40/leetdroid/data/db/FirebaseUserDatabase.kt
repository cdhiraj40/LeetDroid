package com.cdhiraj40.leetdroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cdhiraj40.leetdroid.data.dao.FirebaseUserDao
import com.cdhiraj40.leetdroid.data.entitiy.FirebaseUserProfile

@Database(entities = [FirebaseUserProfile::class], version = 1)
abstract class FirebaseUserDatabase : RoomDatabase() {

    abstract fun firebaseUserDao(): FirebaseUserDao

    companion object {
        private var INSTANCE: FirebaseUserDatabase? = null
        fun getInstance(context: Context): FirebaseUserDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FirebaseUserDatabase::class.java,
                    "firebase_user.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}
