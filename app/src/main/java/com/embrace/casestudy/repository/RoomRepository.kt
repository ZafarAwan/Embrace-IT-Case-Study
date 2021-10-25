package com.embrace.casestudy.repository

import com.embrace.casestudy.model.QuestionAnswerListDB
import com.embrace.casestudy.model.TopScores
import com.test.newimplementation.Network.RoomQuizDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepository @Inject constructor(var roomQuizDao: RoomQuizDao) {

    suspend fun insertedAllQuestion(questionAnswerListDB: QuestionAnswerListDB) =
        withContext(Dispatchers.IO)
        {
            roomQuizDao.insertAllQuestions(questionAnswerListDB)
        }

    fun loadAll() = roomQuizDao.getQuestionList()

    fun getTopScore() = roomQuizDao.getTopScores()

    suspend fun insertedSingleScore(scores: TopScores) = withContext(Dispatchers.IO)
    {
        roomQuizDao.insertSingleScore(scores)
    }

    suspend fun updateSingleScore(scores: TopScores) = withContext(Dispatchers.IO)
    {
        roomQuizDao.singleUpdate(scores)
    }

}