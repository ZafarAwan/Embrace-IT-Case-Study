package com.embrace.casestudy.utils

import com.embrace.casestudy.model.Question
import com.embrace.casestudy.model.QuestionAnswers

sealed class ApiState
{
    object  Loading :ApiState()
    class Failure(val msg:Throwable) :ApiState()
    class Success(val data:QuestionAnswers):ApiState()
    object  Empty :ApiState()
}

