package com.embrace.casestudy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopScores(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val topScore: Int
)
