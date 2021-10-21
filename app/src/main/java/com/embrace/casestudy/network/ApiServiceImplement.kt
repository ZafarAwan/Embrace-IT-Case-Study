package com.embrace.casestudy.network

import com.embrace.casestudy.model.Question
import javax.inject.Inject

class ApiServiceImplement @Inject constructor(private val apiService: ApiService) {

    suspend fun getQuestionList(): List<Question> = apiService.getQuestionDetails()
}