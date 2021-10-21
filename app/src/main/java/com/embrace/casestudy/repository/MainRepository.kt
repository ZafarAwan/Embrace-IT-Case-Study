package com.embrace.casestudy.repository

import com.embrace.casestudy.model.Question
import com.embrace.casestudy.network.ApiServiceImplement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiServiceImplementation: ApiServiceImplement) {

    fun getQuestionsData(): Flow<List<Question>> = flow {
        emit(apiServiceImplementation.getQuestionList())
    }.flowOn(Dispatchers.IO)
}