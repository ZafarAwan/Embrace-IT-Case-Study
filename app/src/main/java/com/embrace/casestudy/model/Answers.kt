package com.embrace.casestudy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answers(
    val A: String?,
    val B: String?,
    val C: String?,
    val D: String?,
    val E: String?
):Parcelable

