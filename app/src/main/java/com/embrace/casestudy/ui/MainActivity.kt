package com.embrace.casestudy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.embrace.casestudy.R
import com.embrace.casestudy.databinding.ActivityMainBinding
import com.embrace.casestudy.model.Question
import com.embrace.casestudy.model.QuestionAnswerListDB
import com.embrace.casestudy.model.QuestionAnswers
import com.embrace.casestudy.model.TopScores
import com.embrace.casestudy.utils.ApiState
import com.embrace.casestudy.utils.NetworkComponents
import com.embrace.casestudy.viewModel.MainViewModel
import com.embrace.casestudy.viewModel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private val mainRoomViewModel: RoomViewModel by viewModels()

    private lateinit var question: QuestionAnswers
    private var topScores: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainRoomViewModel.questionList.observe(this) {
            if (it == null) {
                if (NetworkComponents.DETECT_INTERNET_CONNECTION(this)) {
                    mainViewModel.getQuestionList()
                } else {
                    NetworkComponents.showDialog(this)
                }
            } else {
                var questionAnswers = QuestionAnswers(it.questionList)
                question = questionAnswers
            }
        }

        mainRoomViewModel.topScores.observe(this) {
            if (it == null) {
                var topString = TopScores(0, 0)
                mainRoomViewModel.insertSingleScore(topString)
            } else {
                topScores = it.topScore
                scoreViewUpdate()
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel._questionStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {

                    }
                    is ApiState.Failure -> {

                        Log.e("TAG", "onCreate: " + it.msg)
                    }
                    is ApiState.Success -> {
                        question = it.data
                        addDataInRoomDataBase(it.data.questionList)

                    }
                    is ApiState.Empty -> {

                    }
                }
            }
        }

        initMainView()
    }

    private fun addDataInRoomDataBase(questionList: List<Question>) {
        var questionAnswerListDB = QuestionAnswerListDB(0, questionList)
        mainRoomViewModel.insertAll(questionAnswerListDB)
    }

    private fun initMainView() {
        binding.tvTitle.text = resources.getString(R.string.app_name)
        scoreViewUpdate()

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, QuestionAnswerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(NetworkComponents.questionList, question)
            bundle.putInt(NetworkComponents.topScore, topScores)
            intent.putExtra(NetworkComponents.bundle, bundle)

            startActivity(intent)
        }
    }

    private fun scoreViewUpdate() {
        binding.tvHighScores.text = "$topScores"
    }
}