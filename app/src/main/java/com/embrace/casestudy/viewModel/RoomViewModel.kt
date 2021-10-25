package com.embrace.casestudy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embrace.casestudy.model.QuestionAnswerListDB
import com.embrace.casestudy.model.TopScores
import com.embrace.casestudy.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(var roomRepository: RoomRepository) : ViewModel() {

    val questionList = roomRepository.loadAll()

    val topScores = roomRepository.getTopScore()

    fun insertAll(questionAnswerListDB: QuestionAnswerListDB) = viewModelScope.launch {
        roomRepository.insertedAllQuestion(questionAnswerListDB)
    }

    fun insertSingleScore(topScores: TopScores) = viewModelScope.launch {
        roomRepository.insertedSingleScore(topScores)
    }

    fun updateSingleScore(topScores: TopScores) = viewModelScope.launch {
        roomRepository.updateSingleScore(topScores)
    }

}