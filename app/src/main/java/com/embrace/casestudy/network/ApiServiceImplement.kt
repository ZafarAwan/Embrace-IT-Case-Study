package com.embrace.casestudy.network

import com.embrace.casestudy.model.QuestionAnswers
import javax.inject.Inject

class ApiServiceImplement @Inject constructor(private val apiService: ApiService) {

    suspend fun getQuestionList(): QuestionAnswers = apiService.getQuestionDetails()
}