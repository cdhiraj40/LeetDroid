package com.example.leetdroid.data.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firebase_user")
data class FirebaseUserProfile(
    @ColumnInfo(name = "uuid")
    val uuid: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "username")
    val username: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}