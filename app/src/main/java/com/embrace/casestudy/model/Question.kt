package com.embrace.casestudy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    val question: String?,
    val answers: Answers?,
    val correctAnswer: String?,
    val questionImageUrl: String?,
    val score: Int?,
    val type: String?
):Parcelable
