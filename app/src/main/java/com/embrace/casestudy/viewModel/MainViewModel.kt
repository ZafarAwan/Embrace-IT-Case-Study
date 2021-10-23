package com.embrace.casestudy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embrace.casestudy.repository.MainRepository
import com.embrace.casestudy.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val questionStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _questionStateFlow: StateFlow<ApiState> = questionStateFlow

    fun getQuestionList() = viewModelScope.launch {
        questionStateFlow.value = ApiState.Loading
        mainRepository.getQuestionsData().catch { e ->
            questionStateFlow.value = ApiState.Failure(e)
        }.collect {
            questionStateFlow.value = ApiState.Success(it)
        }
    }
}