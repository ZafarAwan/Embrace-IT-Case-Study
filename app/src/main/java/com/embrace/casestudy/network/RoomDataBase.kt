package com.test.newimplementation.Network

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.embrace.casestudy.model.QuestionAnswerListDB
import com.embrace.casestudy.model.QuestionListTypeConverter
import com.embrace.casestudy.model.TopScores

@Database(
  entities = [QuestionAnswerListDB::class, TopScores::class],
  version = 1,
  exportSchema = false
)
@TypeConverters(QuestionListTypeConverter::class)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun getQuestionDao(): RoomQuizDao
}