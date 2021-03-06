package com.embrace.casestudy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity
data class QuestionAnswerListDB(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val questionList: List<Question>,
)

