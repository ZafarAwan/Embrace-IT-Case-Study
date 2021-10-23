package com.embrace.casestudy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionAnswers(@SerializedName("questions") var questionList: ArrayList<Question>):Parcelable

