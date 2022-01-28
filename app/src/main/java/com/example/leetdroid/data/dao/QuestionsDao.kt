package com.example.leetdroid.data.dao

import androidx.room.*
import androidx.room.Dao
import com.example.leetdroid.data.entitiy.AllQuestions

@Dao
interface QuestionsDao {
    // below method is use to
    // add data to database.
    @Insert
    fun insert(model: AllQuestions?)

    // below method is use to update
    // the data in our database.
    @Update
    fun update(model: AllQuestions?)

    // below line is use to delete a
    // specific course in our database.
//    @Delete
//    fun delete(model: AllQuestions?)

    // on below line we are making query to
    // delete all courses from our database.
//    @Query("DELETE FROM all_questions")
//    fun deleteAllCourses()

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM all_questions")
    suspend fun getAllQuestions():List<AllQuestions>
}