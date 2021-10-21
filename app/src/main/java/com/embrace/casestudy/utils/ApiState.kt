package com.embrace.casestudy.utils

import com.embrace.casestudy.model.Question

sealed class ApiState
{
    object  Loading :ApiState()
    class Failure(val msg:Throwable) :ApiState()
    class Success(val data:List<Question>):ApiState()
    object  Empty :ApiState()
}

