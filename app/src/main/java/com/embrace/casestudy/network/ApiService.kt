package com.embrace.casestudy.network

import com.embrace.casestudy.model.Question
import retrofit2.http.GET

interface ApiService {

    @GET("3acef828-7f8f-4905-a12e-1b057db45f48")
    suspend fun getQuestionDetails(): List<Question>
}