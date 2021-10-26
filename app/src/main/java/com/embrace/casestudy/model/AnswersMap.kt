package com.embrace.casestudy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswersMap(var key:String,var value:String): Parcelable
