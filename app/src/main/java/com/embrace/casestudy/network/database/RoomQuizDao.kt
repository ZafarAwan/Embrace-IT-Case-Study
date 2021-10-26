package com.embrace.casestudy.network.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.embrace.casestudy.model.QuestionAnswerListDB
import com.embrace.casestudy.model.TopScores

@Dao
interface RoomQuizDao {

    @Insert
    fun insertAllQuestions(questions: QuestionAnswerListDB)

    @Insert
    fun insertSingleScore(topScore: TopScores)

    @Update
    fun singleUpdate(topScore: TopScores)

    @Query("Select * from TopScores")
    fun getTopScores(): LiveData<TopScores>

    @Query("Select * from QuestionAnswerListDB")
    fun getQuestionList(): LiveData<QuestionAnswerListDB>

}