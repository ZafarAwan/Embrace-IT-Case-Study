package com.embrace.casestudy.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class QuestionListTypeConverter {

    @TypeConverter
    fun listToJson(value: List<Question>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Question>::class.java).toList()
}