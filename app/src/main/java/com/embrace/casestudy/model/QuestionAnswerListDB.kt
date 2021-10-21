package com.embrace.casestudy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionAnswerListDB(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val question: String,
    val answers: String,
    val correctAnswer: String,
    val questionImageUrl: String?,
    val score: Int,
    val type: String,
    var topScore: String
)
