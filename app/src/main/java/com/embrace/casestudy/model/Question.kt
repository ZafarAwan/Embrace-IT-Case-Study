package com.embrace.casestudy.model

data class Question(
    val question: String,
    val answers: Answers,
    val correctAnswer: String,
    val questionImageUrl: String?,
    val score: Int,
    val type: String
)

data class Answers(
    val A: String,
    val B: String,
    val C: String,
    val D: String,
    val E: String
)